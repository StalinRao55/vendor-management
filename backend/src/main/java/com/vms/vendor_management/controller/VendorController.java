package com.vms.vendor_management.controller;

import com.vms.vendor_management.model.Vendor;
import com.vms.vendor_management.model.enums.VendorStatus;
import com.vms.vendor_management.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin("*")
public class VendorController {

    private final VendorService vendorService;
    
    @Autowired
    private com.vms.vendor_management.service.VendorRegistrationService vendorRegistrationService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendor(@PathVariable Long id) {
        return ResponseEntity.ok(vendorService.getVendorById(id));
    }

    // Admin endpoints for vendor management
    @GetMapping("/pending")
    public ResponseEntity<List<Vendor>> getPendingVendors() {
        return ResponseEntity.ok(vendorRegistrationService.getPendingVendors());
    }

    @GetMapping("/approved")
    public ResponseEntity<List<Vendor>> getApprovedVendors() {
        return ResponseEntity.ok(vendorRegistrationService.getApprovedVendors());
    }

    @GetMapping("/rejected")
    public ResponseEntity<List<Vendor>> getRejectedVendors() {
        return ResponseEntity.ok(vendorRegistrationService.getRejectedVendors());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Vendor>> getVendorsByStatus(@PathVariable String status) {
        try {
            VendorStatus vendorStatus = VendorStatus.valueOf(status.toUpperCase());
            List<Vendor> vendors = vendorRegistrationService.getVendorsByStatus(vendorStatus);
            return ResponseEntity.ok(vendors);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
