package com.vms.vendor_management.repository;

import com.vms.vendor_management.model.PurchaseRequest;
import com.vms.vendor_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    List<PurchaseRequest> findByCreatedBy(User createdBy);
}
