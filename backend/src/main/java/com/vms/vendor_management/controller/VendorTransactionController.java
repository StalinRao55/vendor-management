package com.vms.vendor_management.controller;

import com.vms.vendor_management.model.VendorTransaction;
import com.vms.vendor_management.service.VendorTransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class VendorTransactionController {

    private final VendorTransactionService transactionService;

    public VendorTransactionController(VendorTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/vendors/{vendorId}/transactions")
    public ResponseEntity<VendorTransaction> createTransaction(@PathVariable Long vendorId,
                                                               @Valid @RequestBody VendorTransaction transaction) {
        return ResponseEntity.ok(transactionService.createTransaction(vendorId, transaction));
    }

    @GetMapping("/vendors/{vendorId}/transactions")
    public ResponseEntity<List<VendorTransaction>> getTransactionsByVendor(@PathVariable Long vendorId) {
        return ResponseEntity.ok(transactionService.getTransactionsByVendor(vendorId));
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<VendorTransaction> getTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PutMapping("/transactions/{id}")
    public ResponseEntity<VendorTransaction> updateTransaction(@PathVariable Long id,
                                                               @Valid @RequestBody VendorTransaction transaction) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transaction));
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Transaction deleted successfully");
    }
}
