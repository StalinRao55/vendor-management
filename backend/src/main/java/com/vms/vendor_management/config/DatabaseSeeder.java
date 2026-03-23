package com.vms.vendor_management.config;

import com.vms.vendor_management.model.User;
import com.vms.vendor_management.model.Vendor;
import com.vms.vendor_management.model.enums.Role;
import com.vms.vendor_management.repository.UserRepository;
import com.vms.vendor_management.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UserRepository userRepository, VendorRepository vendorRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            String encodedPassword = passwordEncoder.encode("password");

            // Admin
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encodedPassword);
            admin.setEmail("admin@vms.com");
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);

            // Employee (Procurement Manager)
            User employee = new User();
            employee.setUsername("employee");
            employee.setPassword(encodedPassword);
            employee.setEmail("employee@vms.com");
            employee.setRole(Role.PROCUREMENT_MANAGER);
            employee.setActive(true);
            userRepository.save(employee);

            // Vendor User
            User vendorUser = new User();
            vendorUser.setUsername("vendor");
            vendorUser.setPassword(encodedPassword);
            vendorUser.setEmail("vendor@tech.com");
            vendorUser.setRole(Role.VENDOR);
            vendorUser.setActive(true);
            userRepository.save(vendorUser);

            // Vendor Entity
            if (vendorRepository.count() == 0) {
                Vendor vendor = new Vendor();
                vendor.setName("Tech Corp Vendors");
                vendor.setCompanyName("Tech Corp Vendors Ltd");
                vendor.setEmail("vendor@tech.com");
                vendor.setPhone("1234567890");
                vendor.setServiceType("IT Hardware");
                vendor.setCategory(com.vms.vendor_management.model.enums.VendorCategory.IT_SERVICES);
                vendor.setState("California");
                vendor.setPayment(java.math.BigDecimal.valueOf(1500.0));
                vendor.setPanNumber("ABCDE1234F");
                vendor.setCountry("USA");
                vendor.setGstNumber("22AAAAA0000A1Z5");
                vendor.setCity("San Francisco");
                vendor.setPincode("94107");
                vendor.setAddress("123 Tech Lane");
                vendor.setActive(true);
                vendor.setRegistrationDate(java.time.LocalDate.now());
                vendor.setStatus(com.vms.vendor_management.model.enums.VendorStatus.APPROVED);
                vendorRepository.save(vendor);
            }
        }
    }
}
