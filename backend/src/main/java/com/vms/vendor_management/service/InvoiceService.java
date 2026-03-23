package com.vms.vendor_management.service;

import com.vms.vendor_management.exception.ResourceNotFoundException;
import com.vms.vendor_management.model.Invoice;
import com.vms.vendor_management.model.PurchaseOrder;
import com.vms.vendor_management.model.User;
import com.vms.vendor_management.model.Vendor;
import com.vms.vendor_management.model.enums.InvoiceStatus;
import com.vms.vendor_management.repository.InvoiceRepository;
import com.vms.vendor_management.repository.PurchaseOrderRepository;
import com.vms.vendor_management.repository.UserRepository;
import com.vms.vendor_management.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private UserRepository userRepository;

    public Invoice createInvoice(Invoice invoice, Long vendorId, Long purchaseOrderId, Long createdById) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + vendorId));

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with id: " + purchaseOrderId));

        User createdBy = userRepository.findById(createdById)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + createdById));

        // Generate invoice number
        if (invoice.getInvoiceNumber() == null || invoice.getInvoiceNumber().isEmpty()) {
            invoice.setInvoiceNumber(generateInvoiceNumber());
        }

        invoice.setVendor(vendor);
        invoice.setPurchaseOrder(purchaseOrder);
        invoice.setCreatedBy(createdBy);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setStatus(InvoiceStatus.DRAFT);

        return invoiceRepository.save(invoice);
    }

    public Invoice submitInvoice(Long id, Long submittedById) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        if (invoice.getStatus() != InvoiceStatus.DRAFT) {
            throw new IllegalStateException("Invoice cannot be submitted in current status: " + invoice.getStatus());
        }

        User submittedBy = userRepository.findById(submittedById)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + submittedById));

        invoice.setStatus(InvoiceStatus.SUBMITTED);
        invoice.setSubmittedAt(LocalDateTime.now());

        return invoiceRepository.save(invoice);
    }

    public Invoice approveInvoice(Long id, Long approvedById) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        if (invoice.getStatus() != InvoiceStatus.SUBMITTED && invoice.getStatus() != InvoiceStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Invoice cannot be approved in current status: " + invoice.getStatus());
        }

        User approvedBy = userRepository.findById(approvedById)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + approvedById));

        invoice.setStatus(InvoiceStatus.APPROVED);
        invoice.setApprovedAt(LocalDateTime.now());
        invoice.setApprovedBy(approvedBy);

        return invoiceRepository.save(invoice);
    }

    public Invoice rejectInvoice(Long id, String reason, Long rejectedById) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        if (invoice.getStatus() == InvoiceStatus.PAID || invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new IllegalStateException("Invoice cannot be rejected in current status: " + invoice.getStatus());
        }

        User rejectedBy = userRepository.findById(rejectedById)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + rejectedById));

        invoice.setStatus(InvoiceStatus.CANCELLED);
        invoice.setRejectedAt(LocalDateTime.now());
        invoice.setRejectedBy(rejectedBy);
        invoice.setRejectionReason(reason);

        return invoiceRepository.save(invoice);
    }

    public Invoice markAsPaid(Long id, String paymentMethod, String transactionReference, Long paidById) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        if (invoice.getStatus() != InvoiceStatus.APPROVED) {
            throw new IllegalStateException("Invoice must be approved before marking as paid");
        }

        User paidBy = userRepository.findById(paidById)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + paidById));

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaymentDate(LocalDate.now());
        invoice.setPaidAt(LocalDateTime.now());
        invoice.setPaidBy(paidBy);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setTransactionReference(transactionReference);

        return invoiceRepository.save(invoice);
    }

    public Invoice updateInvoice(Long id, Invoice invoiceDetails) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        invoice.setDescription(invoiceDetails.getDescription());
        invoice.setDueDate(invoiceDetails.getDueDate());
        invoice.setAmount(invoiceDetails.getAmount());
        invoice.setTaxAmount(invoiceDetails.getTaxAmount());
        invoice.setNotes(invoiceDetails.getNotes());

        return invoiceRepository.save(invoice);
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public List<Invoice> getInvoicesByVendor(Long vendorId) {
        return invoiceRepository.findByVendorId(vendorId);
    }

    public List<Invoice> getInvoicesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return invoiceRepository.findByCreatedByOrderByCreatedAtDesc(user);
    }

    public List<Invoice> getInvoicesByStatus(InvoiceStatus status) {
        return invoiceRepository.findByStatus(status);
    }

    public List<Invoice> getOverdueInvoices() {
        return invoiceRepository.findByDueDateBefore(LocalDate.now());
    }

    public List<Invoice> getInvoicesByPurchaseOrder(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with id: " + purchaseOrderId));
        return invoiceRepository.findByPurchaseOrder(purchaseOrder);
    }

    public List<Invoice> searchInvoices(String keyword) {
        return invoiceRepository.findByKeyword(keyword);
    }

    public Long countByStatus(InvoiceStatus status) {
        return invoiceRepository.countByStatus(status);
    }

    public Long countOverdueInvoices() {
        return invoiceRepository.countOverdueInvoices(LocalDate.now(), List.of(InvoiceStatus.PAID, InvoiceStatus.CANCELLED));
    }

    public Double sumByStatus(InvoiceStatus status) {
        return invoiceRepository.sumByStatus(status);
    }

    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        invoiceRepository.delete(invoice);
    }

    private String generateInvoiceNumber() {
        // Generate invoice number with format INV-YYYYMMDD-XXXX
        String datePrefix = "INV-" + LocalDate.now().toString().replace("-", "");
        Long count = invoiceRepository.count() + 1;
        return datePrefix + "-" + String.format("%04d", count);
    }
}