package com.vms.vendor_management.repository;

import com.vms.vendor_management.model.Vendor;
import com.vms.vendor_management.model.enums.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    
    List<Vendor> findByStatus(VendorStatus status);
    
    @Query("SELECT v FROM Vendor v WHERE v.status = :status")
    List<Vendor> findVendorsByStatus(@Param("status") VendorStatus status);
}
