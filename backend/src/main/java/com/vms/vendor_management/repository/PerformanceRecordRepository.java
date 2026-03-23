package com.vms.vendor_management.repository;

import com.vms.vendor_management.model.PerformanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceRecordRepository extends JpaRepository<PerformanceRecord, Long> {
    List<PerformanceRecord> findByVendorIdOrderByEvaluationDateDesc(Long vendorId);
}
