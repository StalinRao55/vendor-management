package com.vms.vendor_management.repository;

import com.vms.vendor_management.model.Invoice;
import com.vms.vendor_management.model.PurchaseOrder;
import com.vms.vendor_management.model.User;
import com.vms.vendor_management.model.enums.InvoiceStatus;
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
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByVendorId(Long vendorId);

    List<Invoice> findByCreatedByOrderByCreatedAtDesc(User createdBy);

    List<Invoice> findByStatus(InvoiceStatus status);

    List<Invoice> findByDueDateBefore(LocalDate date);

    List<Invoice> findByPurchaseOrder(PurchaseOrder purchaseOrder);

    @Query("SELECT i FROM Invoice i WHERE i.invoiceNumber LIKE %:keyword% OR i.description LIKE %:keyword%")
    List<Invoice> findByKeyword(@Param("keyword") String keyword);


    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = :status")
    Long countByStatus(@Param("status") InvoiceStatus status);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.dueDate < :date AND i.status NOT IN :paidStatuses")
    Long countOverdueInvoices(@Param("date") LocalDate date, @Param("paidStatuses") List<InvoiceStatus> paidStatuses);

    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.status = :status")
    Double sumByStatus(@Param("status") InvoiceStatus status);

    @Query("SELECT i FROM Invoice i WHERE i.status IN :statuses ORDER BY i.invoiceDate DESC")
    Page<Invoice> findByStatusInOrderByInvoiceDateDesc(@Param("statuses") List<InvoiceStatus> statuses, Pageable pageable);

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}