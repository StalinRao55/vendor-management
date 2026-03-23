package com.vms.vendor_management.repository;

import com.vms.vendor_management.model.DeliveryTracking;
import com.vms.vendor_management.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeliveryTrackingRepository extends JpaRepository<DeliveryTracking, Long> {

    List<DeliveryTracking> findByPurchaseOrderOrderByTimestampDesc(PurchaseOrder purchaseOrder);

    @Query("SELECT dt FROM DeliveryTracking dt WHERE dt.purchaseOrder = :po ORDER BY dt.timestamp DESC")
    List<DeliveryTracking> findLatestTrackingByPurchaseOrder(@Param("po") PurchaseOrder purchaseOrder);

    @Query("SELECT dt FROM DeliveryTracking dt WHERE dt.purchaseOrder.id = :poId AND dt.timestamp >= :since ORDER BY dt.timestamp DESC")
    List<DeliveryTracking> findByPurchaseOrderSince(@Param("poId") Long poId, @Param("since") LocalDateTime since);

    @Query("SELECT dt FROM DeliveryTracking dt WHERE dt.status = :status ORDER BY dt.timestamp DESC")
    List<DeliveryTracking> findByStatusOrderByTimestampDesc(@Param("status") String status);

    @Query("SELECT dt FROM DeliveryTracking dt WHERE dt.purchaseOrder.vendor.id = :vendorId ORDER BY dt.timestamp DESC")
    List<DeliveryTracking> findByVendorIdOrderByTimestampDesc(@Param("vendorId") Long vendorId);
}