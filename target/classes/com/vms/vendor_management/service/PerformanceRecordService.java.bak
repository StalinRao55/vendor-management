package com.vms.vendor_management.service;

import com.vms.vendor_management.exception.ResourceNotFoundException;
import com.vms.vendor_management.model.PerformanceRecord;
import com.vms.vendor_management.model.Vendor;
import com.vms.vendor_management.repository.PerformanceRecordRepository;
import com.vms.vendor_management.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceRecordService {

    private final PerformanceRecordRepository performanceRecordRepository;
    private final VendorRepository vendorRepository;

    public PerformanceRecordService(PerformanceRecordRepository performanceRecordRepository,
                                    VendorRepository vendorRepository) {
        this.performanceRecordRepository = performanceRecordRepository;
        this.vendorRepository = vendorRepository;
    }

    public PerformanceRecord createRecord(Long vendorId, PerformanceRecord record) {
        Vendor vendor = getVendor(vendorId);
        record.setVendor(vendor);
        return performanceRecordRepository.save(record);
    }

    public List<PerformanceRecord> getRecordsByVendor(Long vendorId) {
        getVendor(vendorId);
        return performanceRecordRepository.findByVendorIdOrderByReviewDateDesc(vendorId);
    }

    public PerformanceRecord getRecordById(Long id) {
        return performanceRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance record not found with id: " + id));
    }

    public PerformanceRecord updateRecord(Long id, PerformanceRecord request) {
        PerformanceRecord existing = getRecordById(id);
        existing.setReviewDate(request.getReviewDate());
        existing.setRating(request.getRating());
        existing.setComments(request.getComments());
        return performanceRecordRepository.save(existing);
    }

    public void deleteRecord(Long id) {
        PerformanceRecord existing = getRecordById(id);
        performanceRecordRepository.delete(existing);
    }

    private Vendor getVendor(Long vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + vendorId));
    }
}
