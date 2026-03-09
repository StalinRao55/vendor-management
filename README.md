# Vendor Management System

A Spring Boot application for managing vendors, contracts, performance records, and transactions.

## Features

- Vendor Management
- Contract Management
- Performance Tracking
- Transaction Management
- H2 Database for local development
- PostgreSQL support for production

## Local Development

### Prerequisites

- Java 17
- Maven

### Running Locally

1. Clone the repository
2. Navigate to the project directory
3. Run the application:

```bash
mvn spring-boot:run
```

4. Access the application at http://localhost:8080
5. Access H2 Console at http://localhost:8080/h2-console

## Docker Deployment

### Building the Docker Image

```bash
docker build -t vendor-management .
```

### Running with Docker

```bash
docker run -p 8080:8080 vendor-management
```

## Render Deployment

This application is configured for deployment on [Render](https://render.com).

### Deployment Steps

1. Connect your GitHub repository to Render
2. Render will automatically detect the `render.yaml` file
3. The application will be deployed using Docker
4. Environment variables will be set automatically

### Environment Variables

- `SPRING_PROFILES_ACTIVE`: Set to "prod" for production
- `SERVER_PORT`: Server port (default: 10000)

## Database Configuration

### Local Development (H2)
- In-memory database
- No external database required
- Console available at `/h2-console`

### Production (Aiven MySQL)
- Uses Aiven MySQL database service
- Connection details configured via environment variables
- SSL enabled for secure connections

## Technologies Used

- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (Development)
- PostgreSQL (Production)
- Spring Security
- Thymeleaf
- Maven

## API Endpoints

- `/` - Home page
- `/vendors` - Vendor management
- `/contracts` - Contract management
- `/performance` - Performance records
- `/transactions` - Transaction management

## License

This project is licensed under the MIT License.