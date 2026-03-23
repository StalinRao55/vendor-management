package com.vms.vendor_management.service;

import com.vms.vendor_management.exception.ResourceNotFoundException;
import com.vms.vendor_management.model.Vendor;
import com.vms.vendor_management.model.VendorTransaction;
import com.vms.vendor_management.repository.VendorRepository;
import com.vms.vendor_management.repository.VendorTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorTransactionService {

    private final VendorTransactionRepository transactionRepository;
    private final VendorRepository vendorRepository;

    public VendorTransactionService(VendorTransactionRepository transactionRepository,
                                    VendorRepository vendorRepository) {
        this.transactionRepository = transactionRepository;
        this.vendorRepository = vendorRepository;
    }

    public VendorTransaction createTransaction(Long vendorId, VendorTransaction transaction) {
        Vendor vendor = getVendor(vendorId);
        transaction.setVendor(vendor);
        return transactionRepository.save(transaction);
    }

    public List<VendorTransaction> getTransactionsByVendor(Long vendorId) {
        getVendor(vendorId);
        return transactionRepository.findByVendorIdOrderByTransactionDateDesc(vendorId);
    }

    public VendorTransaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
    }

    public VendorTransaction updateTransaction(Long id, VendorTransaction request) {
        VendorTransaction existing = getTransactionById(id);
        existing.setTransactionDate(request.getTransactionDate());
        existing.setAmount(request.getAmount());
        existing.setType(request.getType());
        existing.setStatus(request.getStatus());
        existing.setReference(request.getReference());
        return transactionRepository.save(existing);
    }

    public void deleteTransaction(Long id) {
        VendorTransaction existing = getTransactionById(id);
        transactionRepository.delete(existing);
    }

    private Vendor getVendor(Long vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + vendorId));
    }
}
