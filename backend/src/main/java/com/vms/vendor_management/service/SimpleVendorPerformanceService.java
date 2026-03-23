package com.vms.vendor_management.service;

import com.vms.vendor_management.model.PerformanceRecord;
import com.vms.vendor_management.model.Vendor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleVendorPerformanceService {

    public PerformanceRecord createPerformanceRecord(Long vendorId, Integer qualityScore, 
                                                    Integer deliveryScore, Integer serviceScore,
                                                    String comments, Long evaluatedById) {
        PerformanceRecord performanceRecord = new PerformanceRecord();
        performanceRecord.setQualityScore(qualityScore);
        performanceRecord.setDeliveryScore(deliveryScore);
        performanceRecord.setServiceScore(serviceScore);
        performanceRecord.setComments(comments);
        performanceRecord.setEvaluationDate(LocalDate.now());
        
        // Calculate overall score
        Double overallScore = (qualityScore + deliveryScore + serviceScore) / 3.0;
        performanceRecord.setOverallScore(overallScore);

        return performanceRecord;
    }

    public List<PerformanceRecord> getPerformanceRecordsByVendor(Long vendorId) {
        return new ArrayList<>();
    }

    public List<PerformanceRecord> getRecentPerformanceRecords(int limit) {
        return new ArrayList<>();
    }
}