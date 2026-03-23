# Aiven PostgreSQL Database Connection Guide

## Overview
This guide explains how to connect your Vendor Management System to Aiven PostgreSQL database.

## Current Configuration Status

### ✅ Development Setup (Working)
- **Profile**: `dev` (default)
- **Database**: H2 in-memory database
- **URL**: `jdbc:h2:mem:testdb`
- **Console**: Available at http://localhost:8080/h2-console

### 🔄 Production Setup (Ready for Aiven)
- **Profile**: `prod`
- **Database**: PostgreSQL with Aiven
- **Configuration**: `src/main/resources/database.properties`

## How to Connect to Aiven PostgreSQL

### Step 1: Get Your Aiven Connection Details
1. Log in to your Aiven Console
2. Navigate to your PostgreSQL service
3. Copy the connection details:
   - **Host**: `your-service-name.aivencloud.com`
   - **Port**: Usually `21962`
   - **Database**: Usually `defaultdb`
   - **Username**: Usually `avnadmin`
   - **Password**: Your Aiven service password

### Step 2: Update Database Configuration
Edit `src/main/resources/database.properties`:

```properties
# Aiven PostgreSQL Database Configuration
# Replace the values below with your actual Aiven credentials

# Database Connection
# IMPORTANT: Update these values with your actual Aiven PostgreSQL connection details
spring.datasource.url=jdbc:postgresql://YOUR_ACTUAL_HOST.aivencloud.com:21962/defaultdb?sslmode=require
spring.datasource.username=avnadmin
spring.datasource.password=YOUR_ACTUAL_PASSWORD

# Database Driver
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration for PostgreSQL
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Connection Pool Configuration
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.idle-timeout=30000
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.max-lifetime=1800000

# SSL Configuration for Aiven
spring.datasource.hikari.data-source-properties.ssl=true
spring.datasource.hikari.data-source-properties.sslmode=require
spring.datasource.hikari.data-source-properties.sslfactory=org.postgresql.ssl.NonValidatingFactory
```

### Step 3: Switch to Production Profile
Run the application with the production profile:

```bash
# Option 1: Using Maven
mvn spring-boot:run -Dspring-boot.run.profiles=prod

# Option 2: Using Java
java -jar -Dspring.profiles.active=prod target/vendor-management-1.0.jar

# Option 3: Set environment variable
export SPRING_PROFILES_ACTIVE=prod
mvn spring-boot:run
```

## Configuration Files Overview

### `application.properties` (Main Configuration)
```properties
# Default Profile Configuration
spring.profiles.active=dev  # Change to 'prod' for Aiven
server.port=8080
spring.security.enabled=true
```

### `application-dev.properties` (Development - H2)
```properties
# H2 Database Configuration for Local Development
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```

### `application-prod.properties` (Production - PostgreSQL)
```properties
# Include Aiven Database Configuration
spring.config.import=database.properties
spring.h2.console.enabled=false
```

### `database.properties` (Aiven PostgreSQL Details)
```properties
# PostgreSQL connection details - UPDATE THESE VALUES
spring.datasource.url=jdbc:postgresql://HOST.aivencloud.com:PORT/database?sslmode=require
spring.datasource.username=avnadmin
spring.datasource.password=YOUR_PASSWORD
```

## Troubleshooting

### Common Issues

1. **Connection Timeout**
   - Check your Aiven service is running
   - Verify network connectivity
   - Check firewall settings

2. **SSL Connection Failed**
   - Ensure `sslmode=require` is set
   - Verify SSL factory configuration

3. **Authentication Failed**
   - Double-check username and password
   - Ensure user has proper database permissions

4. **Host Not Found**
   - Verify the hostname is correct
   - Check DNS resolution

### Testing Your Connection

1. **Test with psql**:
   ```bash
   psql "host=your-host.aivencloud.com port=21962 dbname=defaultdb user=avnadmin password=your-password sslmode=require"
   ```

2. **Test with Aiven Console**:
   - Use the "Connect" button in Aiven Console
   - Test connection before updating application

## Security Notes

- **Never commit real passwords** to version control
- Use environment variables for sensitive data in production
- Consider using Aiven's IAM authentication for enhanced security
- Regularly rotate database passwords

## Next Steps

1. Update `database.properties` with your actual Aiven credentials
2. Switch to production profile: `mvn spring-boot:run -Dspring-boot.run.profiles=prod`
3. Verify the application connects successfully
4. Test all database operations (CRUD)
5. Monitor logs for any connection issues

## Support

If you encounter issues:
1. Check Aiven service status
2. Verify network connectivity
3. Review application logs
4. Contact Aiven support if needed