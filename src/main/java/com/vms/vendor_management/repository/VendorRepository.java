package com.vms.vendor_management.repository;

import com.vms.vendor_management.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
