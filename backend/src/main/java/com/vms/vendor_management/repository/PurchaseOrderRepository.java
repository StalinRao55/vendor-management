package com.vms.vendor_management.repository;

import com.vms.vendor_management.model.PurchaseOrder;
import com.vms.vendor_management.model.User;
import com.vms.vendor_management.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    List<PurchaseOrder> findByVendorId(Long vendorId);

    List<PurchaseOrder> findByCreatedByOrderByCreatedAtDesc(User createdBy);

    List<PurchaseOrder> findByStatus(OrderStatus status);

    List<PurchaseOrder> findByExpectedDeliveryDateBefore(LocalDate date);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.poNumber LIKE %:keyword% OR po.description LIKE %:keyword%")
    List<PurchaseOrder> findByKeyword(@Param("keyword") String keyword);


    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.status = :status")
    Long countByStatus(@Param("status") OrderStatus status);

    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.createdAt >= :startDate AND po.createdAt <= :endDate")
    Long countByCreatedAtBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.status IN :statuses ORDER BY po.createdAt DESC")
    Page<PurchaseOrder> findByStatusInOrderByCreatedAtDesc(@Param("statuses") List<OrderStatus> statuses, Pageable pageable);

    Optional<PurchaseOrder> findByPoNumber(String poNumber);
}