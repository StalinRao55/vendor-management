package com.vms.vendor_management.controller;

import com.vms.vendor_management.exception.ResourceNotFoundException;
import com.vms.vendor_management.model.Invoice;
import com.vms.vendor_management.model.enums.InvoiceStatus;
import com.vms.vendor_management.service.InvoiceService;
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
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:3000")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM')")
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'VENDOR')")
    public List<Invoice> getInvoicesByUser(@PathVariable Long userId) {
        return invoiceService.getInvoicesByUser(userId);
    }

    @GetMapping("/vendor/{vendorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'VENDOR')")
    public List<Invoice> getInvoicesByVendor(@PathVariable Long vendorId) {
        return invoiceService.getInvoicesByVendor(vendorId);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM')")
    public List<Invoice> getInvoicesByStatus(@PathVariable InvoiceStatus status) {
        return invoiceService.getInvoicesByStatus(status);
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_TEAM')")
    public List<Invoice> getOverdueInvoices() {
        return invoiceService.getOverdueInvoices();
    }

    @GetMapping("/purchase-order/{purchaseOrderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM')")
    public List<Invoice> getInvoicesByPurchaseOrder(@PathVariable Long purchaseOrderId) {
        return invoiceService.getInvoicesByPurchaseOrder(purchaseOrderId);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM')")
    public List<Invoice> searchInvoices(@RequestParam String keyword) {
        return invoiceService.searchInvoices(keyword);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'VENDOR')")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(invoice);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'VENDOR')")
    public Invoice createInvoice(@RequestBody Invoice invoice,
                               @RequestParam Long vendorId,
                               @RequestParam Long purchaseOrderId,
                               @RequestParam Long createdById) {
        return invoiceService.createInvoice(invoice, vendorId, purchaseOrderId, createdById);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'VENDOR')")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id,
                                               @RequestBody Invoice invoiceDetails) {
        Invoice updatedInvoice = invoiceService.updateInvoice(id, invoiceDetails);
        return ResponseEntity.ok(updatedInvoice);
    }

    @PutMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDOR')")
    public ResponseEntity<Invoice> submitInvoice(@PathVariable Long id,
                                               @RequestParam Long submittedById) {
        Invoice submittedInvoice = invoiceService.submitInvoice(id, submittedById);
        return ResponseEntity.ok(submittedInvoice);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM')")
    public ResponseEntity<Invoice> approveInvoice(@PathVariable Long id,
                                                @RequestParam Long approvedById) {
        Invoice approvedInvoice = invoiceService.approveInvoice(id, approvedById);
        return ResponseEntity.ok(approvedInvoice);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM')")
    public ResponseEntity<Invoice> rejectInvoice(@PathVariable Long id,
                                               @RequestParam String reason,
                                               @RequestParam Long rejectedById) {
        Invoice rejectedInvoice = invoiceService.rejectInvoice(id, reason, rejectedById);
        return ResponseEntity.ok(rejectedInvoice);
    }

    @PutMapping("/{id}/mark-paid")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_TEAM')")
    public ResponseEntity<Invoice> markAsPaid(@PathVariable Long id,
                                            @RequestParam String paymentMethod,
                                            @RequestParam String transactionReference,
                                            @RequestParam Long paidById) {
        Invoice paidInvoice = invoiceService.markAsPaid(id, paymentMethod, transactionReference, paidById);
        return ResponseEntity.ok(paidInvoice);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats/count-by-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_TEAM')")
    public Map<String, Long> getCountByStatus() {
        Map<String, Long> stats = new HashMap<>();
        for (InvoiceStatus status : InvoiceStatus.values()) {
            stats.put(status.name(), invoiceService.countByStatus(status));
        }
        return stats;
    }

    @GetMapping("/stats/overdue-count")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_TEAM')")
    public Long getOverdueCount() {
        return invoiceService.countOverdueInvoices();
    }

    @GetMapping("/stats/sum-by-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_TEAM')")
    public Map<String, Double> getSumByStatus() {
        Map<String, Double> stats = new HashMap<>();
        for (InvoiceStatus status : InvoiceStatus.values()) {
            Double sum = invoiceService.sumByStatus(status);
            stats.put(status.name(), sum != null ? sum : 0.0);
        }
        return stats;
    }
}