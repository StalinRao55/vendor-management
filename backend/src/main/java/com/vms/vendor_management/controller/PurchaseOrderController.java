package com.vms.vendor_management.controller;

import com.vms.vendor_management.exception.ResourceNotFoundException;
import com.vms.vendor_management.model.PurchaseOrder;
import com.vms.vendor_management.model.User;
import com.vms.vendor_management.model.Vendor;
import com.vms.vendor_management.model.enums.OrderStatus;
import com.vms.vendor_management.service.PurchaseOrderService;
import com.vms.vendor_management.service.UserService;
import com.vms.vendor_management.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/purchase-orders")
@CrossOrigin(origins = "http://localhost:3000")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM')")
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderService.getAllPurchaseOrders();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'VENDOR')")
    public List<PurchaseOrder> getPurchaseOrdersByUser(@PathVariable Long userId) {
        return purchaseOrderService.getPurchaseOrdersByUser(userId);
    }

    @GetMapping("/vendor/{vendorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'VENDOR')")
    public List<PurchaseOrder> getPurchaseOrdersByVendor(@PathVariable Long vendorId) {
        return purchaseOrderService.getPurchaseOrdersByVendor(vendorId);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM')")
    public List<PurchaseOrder> getPurchaseOrdersByStatus(@PathVariable OrderStatus status) {
        return purchaseOrderService.getPurchaseOrdersByStatus(status);
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER')")
    public List<PurchaseOrder> getOverdueOrders() {
        return purchaseOrderService.getOverdueOrders();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM')")
    public List<PurchaseOrder> searchPurchaseOrders(@RequestParam String keyword) {
        return purchaseOrderService.searchPurchaseOrders(keyword);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'VENDOR')")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderById(id);
        return ResponseEntity.ok(purchaseOrder);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER')")
    public PurchaseOrder createPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder,
                                           @RequestParam Long vendorId,
                                           @RequestParam Long createdById) {
        return purchaseOrderService.createPurchaseOrder(purchaseOrder, vendorId, createdById);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER')")
    public ResponseEntity<PurchaseOrder> updatePurchaseOrder(@PathVariable Long id,
                                                           @RequestBody PurchaseOrder purchaseOrderDetails) {
        PurchaseOrder updatedPurchaseOrder = purchaseOrderService.updatePurchaseOrder(id, purchaseOrderDetails);
        return ResponseEntity.ok(updatedPurchaseOrder);
    }

    @PutMapping("/{id}/vendor-status")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<PurchaseOrder> updateVendorStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        PurchaseOrder po = purchaseOrderService.getPurchaseOrderById(id);
        po.setStatus(status);
        return ResponseEntity.ok(purchaseOrderService.updatePurchaseOrder(id, po));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER')")
    public ResponseEntity<PurchaseOrder> approvePurchaseOrder(@PathVariable Long id,
                                                            @RequestParam Long approvedById) {
        PurchaseOrder approvedPurchaseOrder = purchaseOrderService.approvePurchaseOrder(id, approvedById);
        return ResponseEntity.ok(approvedPurchaseOrder);
    }

    @PutMapping("/{id}/send-to-vendor")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER')")
    public ResponseEntity<PurchaseOrder> sendToVendor(@PathVariable Long id,
                                                    @RequestParam Long sentById) {
        PurchaseOrder sentPurchaseOrder = purchaseOrderService.sendToVendor(id, sentById);
        return ResponseEntity.ok(sentPurchaseOrder);
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER')")
    public ResponseEntity<PurchaseOrder> markAsCompleted(@PathVariable Long id) {
        PurchaseOrder completedPurchaseOrder = purchaseOrderService.markAsCompleted(id);
        return ResponseEntity.ok(completedPurchaseOrder);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER')")
    public ResponseEntity<PurchaseOrder> cancelPurchaseOrder(@PathVariable Long id,
                                                           @RequestParam String reason,
                                                           @RequestParam Long cancelledById) {
        PurchaseOrder cancelledPurchaseOrder = purchaseOrderService.cancelPurchaseOrder(id, reason, cancelledById);
        return ResponseEntity.ok(cancelledPurchaseOrder);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deletePurchaseOrder(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats/count-by-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER')")
    public Map<String, Long> getCountByStatus() {
        Map<String, Long> stats = new HashMap<>();
        for (OrderStatus status : OrderStatus.values()) {
            stats.put(status.name(), purchaseOrderService.countByStatus(status));
        }
        return stats;
    }

    @GetMapping("/stats/count-by-date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER')")
    public Long getCountByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        // Parse dates and call service method
        // This is a simplified implementation
        return 0L;
    }
}