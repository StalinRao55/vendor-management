package com.vms.vendor_management.service;

import com.vms.vendor_management.model.Vendor;
import com.vms.vendor_management.model.enums.VendorStatus;
import com.vms.vendor_management.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VendorRegistrationService {

    private final VendorRepository vendorRepository;
    private final NotificationService notificationService;
    
    // Directory to store uploaded documents
    private final Path fileStorageLocation = Paths.get("uploads/documents");

    @Autowired
    public VendorRegistrationService(VendorRepository vendorRepository, NotificationService notificationService) {
        this.vendorRepository = vendorRepository;
        this.notificationService = notificationService;
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    @Transactional
    public Vendor registerVendor(Vendor vendor, MultipartFile gstFile, MultipartFile panFile, 
                                MultipartFile licenseFile, MultipartFile registrationFile) {
        
        // Set initial status to PENDING_APPROVAL
        vendor.setStatus(VendorStatus.PENDING_APPROVAL);
        vendor.setActive(false);
        vendor.setRegistrationDate(LocalDate.now());
        
        // Save vendor first to get ID
        Vendor savedVendor = vendorRepository.save(vendor);
        
        // Handle file uploads
        try {
            if (gstFile != null && !gstFile.isEmpty()) {
                String gstPath = saveFile(gstFile, "gst_" + savedVendor.getId());
                savedVendor.setDocumentPath(gstPath);
            }
            
            if (panFile != null && !panFile.isEmpty()) {
                String panPath = saveFile(panFile, "pan_" + savedVendor.getId());
                // Store multiple document paths or create a document management system
                // For now, we'll store in a simple way
            }
            
            if (licenseFile != null && !licenseFile.isEmpty()) {
                String licensePath = saveFile(licenseFile, "license_" + savedVendor.getId());
            }
            
            if (registrationFile != null && !registrationFile.isEmpty()) {
                String regPath = saveFile(registrationFile, "reg_" + savedVendor.getId());
            }
            
            // Notify admin about new vendor registration
            // For now, we'll skip the notification creation until we have admin user IDs
            // notificationService.createNotification(
            //     "New Vendor Registration", 
            //     "A new vendor (" + vendor.getName() + ") has registered and requires approval.", 
            //     "VENDOR_APPROVAL"
            // );
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to store vendor documents", e);
        }
        
        return vendorRepository.save(savedVendor);
    }

    public List<Vendor> getVendorsByStatus(VendorStatus status) {
        return vendorRepository.findByStatus(status);
    }

    @Transactional
    public Vendor approveVendor(Long vendorId, Long approvedByUserId) {
        Optional<Vendor> vendorOpt = vendorRepository.findById(vendorId);
        if (vendorOpt.isPresent()) {
            Vendor vendor = vendorOpt.get();
            vendor.setStatus(VendorStatus.APPROVED);
            vendor.setActive(true);
            vendor.setApprovalDate(LocalDate.now());
            
            Vendor updatedVendor = vendorRepository.save(vendor);
            
            // Notify vendor about approval
            // For now, we'll skip the notification creation until we have vendor user IDs
            // notificationService.createNotification(
            //     "Vendor Registration Approved", 
            //     "Your vendor registration has been approved. You can now access the vendor portal.", 
            //     "VENDOR_APPROVAL"
            // );
            
            return updatedVendor;
        }
        throw new RuntimeException("Vendor not found with ID: " + vendorId);
    }

    @Transactional
    public Vendor rejectVendor(Long vendorId, String rejectionReason, Long rejectedByUserId) {
        Optional<Vendor> vendorOpt = vendorRepository.findById(vendorId);
        if (vendorOpt.isPresent()) {
            Vendor vendor = vendorOpt.get();
            vendor.setStatus(VendorStatus.REJECTED);
            vendor.setActive(false);
            
            Vendor updatedVendor = vendorRepository.save(vendor);
            
            // Notify vendor about rejection
            // For now, we'll skip the notification creation until we have vendor user IDs
            // notificationService.createNotification(
            //     "Vendor Registration Rejected", 
            //     "Your vendor registration has been rejected. Reason: " + rejectionReason, 
            //     "VENDOR_REJECTION"
            // );
            
            return updatedVendor;
        }
        throw new RuntimeException("Vendor not found with ID: " + vendorId);
    }

    private String saveFile(MultipartFile file, String fileName) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = fileName + "_" + System.currentTimeMillis() + fileExtension;
        
        Path targetLocation = fileStorageLocation.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        return targetLocation.toString();
    }

    public List<Vendor> getPendingVendors() {
        return getVendorsByStatus(VendorStatus.PENDING_APPROVAL);
    }

    public List<Vendor> getApprovedVendors() {
        return getVendorsByStatus(VendorStatus.APPROVED);
    }

    public List<Vendor> getRejectedVendors() {
        return getVendorsByStatus(VendorStatus.REJECTED);
    }
}