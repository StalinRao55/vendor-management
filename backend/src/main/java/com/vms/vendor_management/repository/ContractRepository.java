package com.vms.vendor_management.repository;

import com.vms.vendor_management.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByVendorIdOrderByStartDateDesc(Long vendorId);
}
