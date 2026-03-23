package com.vms.vendor_management.controller;

import com.vms.vendor_management.model.PurchaseRequest;
import com.vms.vendor_management.model.enums.RequestStatus;
import com.vms.vendor_management.service.PurchaseRequestService;
import com.vms.vendor_management.service.UserService;
import com.vms.vendor_management.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-requests")
@CrossOrigin(origins = "http://localhost:3000")
public class PurchaseRequestController {

    @Autowired
    private PurchaseRequestService purchaseRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM')")
    public List<PurchaseRequest> getAllRequests() {
        return purchaseRequestService.getAllRequests();
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'ADMIN')")
    public PurchaseRequest createRequest(@RequestBody PurchaseRequest request, @RequestParam Long createdById, @RequestParam(required = false) Long vendorId) {
        request.setCreatedBy(userService.getUserById(createdById));
        if(vendorId != null) {
            request.setSuggestedVendor(vendorService.getVendorById(vendorId));
        }
        return purchaseRequestService.createRequest(request);
    }
    
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PurchaseRequest> approveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseRequestService.updateStatus(id, RequestStatus.APPROVED));
    }
    
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PurchaseRequest> rejectRequest(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseRequestService.updateStatus(id, RequestStatus.REJECTED));
    }
}
