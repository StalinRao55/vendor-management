package com.vms.vendor_management.controller;

import jakarta.validation.Valid;
import com.vms.vendor_management.model.Vendor;
import com.vms.vendor_management.model.enums.VendorStatus;
import com.vms.vendor_management.service.VendorRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor-registration")
public class VendorRegistrationController {

    private final VendorRegistrationService vendorRegistrationService;

    @Autowired
    public VendorRegistrationController(VendorRegistrationService vendorRegistrationService) {
        this.vendorRegistrationService = vendorRegistrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerVendor(
            @Valid @ModelAttribute Vendor vendor,
            BindingResult bindingResult,
            @RequestParam(value = "gstFile", required = false) MultipartFile gstFile,
            @RequestParam(value = "panFile", required = false) MultipartFile panFile,
            @RequestParam(value = "licenseFile", required = false) MultipartFile licenseFile,
            @RequestParam(value = "registrationFile", required = false) MultipartFile registrationFile) {
        if (bindingResult.hasErrors()) {
            Map<String, String> details = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                details.put(error.getField(), error.getDefaultMessage());
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Please complete all required fields.");
            response.put("details", details);
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Vendor registeredVendor = vendorRegistrationService.registerVendor(
                vendor, gstFile, panFile, licenseFile, registrationFile
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Vendor registration submitted successfully. Awaiting approval.");
            response.put("vendor", registeredVendor);
            
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "A vendor with the same email, GST number, or PAN number already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to register vendor: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Vendor>> getPendingVendors() {
        List<Vendor> pendingVendors = vendorRegistrationService.getPendingVendors();
        return ResponseEntity.ok(pendingVendors);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<Vendor>> getApprovedVendors() {
        List<Vendor> approvedVendors = vendorRegistrationService.getApprovedVendors();
        return ResponseEntity.ok(approvedVendors);
    }

    @GetMapping("/rejected")
    public ResponseEntity<List<Vendor>> getRejectedVendors() {
        List<Vendor> rejectedVendors = vendorRegistrationService.getRejectedVendors();
        return ResponseEntity.ok(rejectedVendors);
    }

    @PutMapping("/{vendorId}/approve")
    public ResponseEntity<Map<String, Object>> approveVendor(
            @PathVariable Long vendorId,
            @RequestParam(value = "approvedByUserId", required = false) Long approvedByUserId) {
        
        try {
            Vendor approvedVendor = vendorRegistrationService.approveVendor(vendorId, approvedByUserId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Vendor approved successfully.");
            response.put("vendor", approvedVendor);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to approve vendor: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{vendorId}/reject")
    public ResponseEntity<Map<String, Object>> rejectVendor(
            @PathVariable Long vendorId,
            @RequestParam("rejectionReason") String rejectionReason,
            @RequestParam(value = "rejectedByUserId", required = false) Long rejectedByUserId) {
        
        try {
            Vendor rejectedVendor = vendorRegistrationService.rejectVendor(vendorId, rejectionReason, rejectedByUserId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Vendor rejected successfully.");
            response.put("vendor", rejectedVendor);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to reject vendor: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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
