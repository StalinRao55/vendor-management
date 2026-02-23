package com.vms.vendor_management.repository;

import com.vms.vendor_management.model.VendorTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorTransactionRepository extends JpaRepository<VendorTransaction, Long> {
    List<VendorTransaction> findByVendorIdOrderByTransactionDateDesc(Long vendorId);
}
