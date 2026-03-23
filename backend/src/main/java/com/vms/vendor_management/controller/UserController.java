package com.vms.vendor_management.controller;

import com.vms.vendor_management.model.Vendor;
import com.vms.vendor_management.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private com.vms.vendor_management.service.UserService userService;

    @GetMapping("/user")
    public Map<String, Object> getCurrentUser(Authentication authentication) {
        Map<String, Object> userInfo = new HashMap<>();
        if (authentication != null) {
            userInfo.put("username", authentication.getName());
            com.vms.vendor_management.model.User currentUser = userService.getUserByUsername(authentication.getName());
            if (currentUser != null) {
                userInfo.put("id", currentUser.getId());
                userInfo.put("role", currentUser.getRole().name());
                if ("VENDOR".equals(currentUser.getRole().name())) {
                    vendorService.getAllVendors().stream()
                        .filter(v -> v.getEmail() != null && v.getEmail().equalsIgnoreCase(currentUser.getEmail()))
                        .findFirst()
                        .ifPresent(v -> userInfo.put("vendorId", v.getId()));
                }
            }
            userInfo.put("isAdmin", authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals("ROLE_ADMIN")));
        }
        return userInfo;
    }

    // Admin CRUD operations for vendors

    @PostMapping("/admin/vendors")
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Vendor createdVendor = vendorService.createVendor(vendor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVendor);
    }

    @GetMapping("/admin/vendors")
    public ResponseEntity<List<Vendor>> getAllVendors(Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Vendor> vendors = vendorService.getAllVendors();
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/admin/vendors/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Long id, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            Vendor vendor = vendorService.getVendorById(id);
            return ResponseEntity.ok(vendor);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/admin/vendors/{id}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable Long id, @RequestBody Vendor vendor, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            Vendor updatedVendor = vendorService.updateVendor(id, vendor);
            return ResponseEntity.ok(updatedVendor);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/admin/vendors/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable Long id, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            vendorService.deleteVendor(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
    }
}
