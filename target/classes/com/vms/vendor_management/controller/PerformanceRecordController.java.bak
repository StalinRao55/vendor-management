package com.vms.vendor_management.controller;

import com.vms.vendor_management.model.PerformanceRecord;
import com.vms.vendor_management.service.PerformanceRecordService;
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
public class PerformanceRecordController {

    private final PerformanceRecordService performanceRecordService;

    public PerformanceRecordController(PerformanceRecordService performanceRecordService) {
        this.performanceRecordService = performanceRecordService;
    }

    @PostMapping("/vendors/{vendorId}/performance")
    public ResponseEntity<PerformanceRecord> createRecord(@PathVariable Long vendorId,
                                                          @Valid @RequestBody PerformanceRecord record) {
        return ResponseEntity.ok(performanceRecordService.createRecord(vendorId, record));
    }

    @GetMapping("/vendors/{vendorId}/performance")
    public ResponseEntity<List<PerformanceRecord>> getRecordsByVendor(@PathVariable Long vendorId) {
        return ResponseEntity.ok(performanceRecordService.getRecordsByVendor(vendorId));
    }

    @GetMapping("/performance/{id}")
    public ResponseEntity<PerformanceRecord> getRecord(@PathVariable Long id) {
        return ResponseEntity.ok(performanceRecordService.getRecordById(id));
    }

    @PutMapping("/performance/{id}")
    public ResponseEntity<PerformanceRecord> updateRecord(@PathVariable Long id,
                                                          @Valid @RequestBody PerformanceRecord record) {
        return ResponseEntity.ok(performanceRecordService.updateRecord(id, record));
    }

    @DeleteMapping("/performance/{id}")
    public ResponseEntity<String> deleteRecord(@PathVariable Long id) {
        performanceRecordService.deleteRecord(id);
        return ResponseEntity.ok("Performance record deleted successfully");
    }
}
