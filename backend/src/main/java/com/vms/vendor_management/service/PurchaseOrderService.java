package com.vms.vendor_management.service;

import com.vms.vendor_management.exception.ResourceNotFoundException;
import com.vms.vendor_management.model.PurchaseOrder;
import com.vms.vendor_management.model.User;
import com.vms.vendor_management.model.Vendor;
import com.vms.vendor_management.model.enums.OrderStatus;
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
import java.util.Optional;

@Service
@Transactional
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private UserRepository userRepository;

    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder, Long vendorId, Long createdById) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + vendorId));

        User createdBy = userRepository.findById(createdById)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + createdById));

        // Generate PO number
        if (purchaseOrder.getPoNumber() == null || purchaseOrder.getPoNumber().isEmpty()) {
            purchaseOrder.setPoNumber(generatePONumber());
        }

        purchaseOrder.setVendor(vendor);
        purchaseOrder.setCreatedBy(createdBy);
        purchaseOrder.setOrderDate(LocalDate.now());
        purchaseOrder.setStatus(OrderStatus.DRAFT);

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder updatePurchaseOrder(Long id, PurchaseOrder purchaseOrderDetails) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with id: " + id));

        purchaseOrder.setDescription(purchaseOrderDetails.getDescription());
        purchaseOrder.setExpectedDeliveryDate(purchaseOrderDetails.getExpectedDeliveryDate());
        purchaseOrder.setDeliveryAddress(purchaseOrderDetails.getDeliveryAddress());
        purchaseOrder.setDeliveryInstructions(purchaseOrderDetails.getDeliveryInstructions());
        purchaseOrder.setUrgent(purchaseOrderDetails.getUrgent());
        purchaseOrder.setPriorityLevel(purchaseOrderDetails.getPriorityLevel());

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder approvePurchaseOrder(Long id, Long approvedById) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with id: " + id));

        if (purchaseOrder.getStatus() != OrderStatus.DRAFT && purchaseOrder.getStatus() != OrderStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Purchase Order cannot be approved in current status: " + purchaseOrder.getStatus());
        }

        User approvedBy = userRepository.findById(approvedById)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + approvedById));

        purchaseOrder.setStatus(OrderStatus.APPROVED);
        purchaseOrder.setApprovedAt(LocalDateTime.now());
        purchaseOrder.setApprovedBy(approvedBy);

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder sendToVendor(Long id, Long sentById) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with id: " + id));

        if (purchaseOrder.getStatus() != OrderStatus.APPROVED) {
            throw new IllegalStateException("Purchase Order must be approved before sending to vendor");
        }

        purchaseOrder.setStatus(OrderStatus.SENT_TO_VENDOR);
        purchaseOrder.setSentToVendorAt(LocalDateTime.now());

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder markAsCompleted(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with id: " + id));

        if (purchaseOrder.getStatus() != OrderStatus.IN_PROGRESS && purchaseOrder.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalStateException("Purchase Order cannot be completed in current status: " + purchaseOrder.getStatus());
        }

        purchaseOrder.setStatus(OrderStatus.COMPLETED);
        purchaseOrder.setCompletedAt(LocalDateTime.now());

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder cancelPurchaseOrder(Long id, String reason, Long cancelledById) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with id: " + id));

        if (purchaseOrder.getStatus() == OrderStatus.COMPLETED || purchaseOrder.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Purchase Order cannot be cancelled in current status: " + purchaseOrder.getStatus());
        }

        User cancelledBy = userRepository.findById(cancelledById)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + cancelledById));

        purchaseOrder.setStatus(OrderStatus.CANCELLED);
        purchaseOrder.setCancelledAt(LocalDateTime.now());
        purchaseOrder.setCancelledBy(cancelledBy);
        purchaseOrder.setCancellationReason(reason);

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with id: " + id));
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public List<PurchaseOrder> getPurchaseOrdersByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return purchaseOrderRepository.findByCreatedByOrderByCreatedAtDesc(user);
    }

    public List<PurchaseOrder> getPurchaseOrdersByVendor(Long vendorId) {
        return purchaseOrderRepository.findByVendorId(vendorId);
    }

    public List<PurchaseOrder> getPurchaseOrdersByStatus(OrderStatus status) {
        return purchaseOrderRepository.findByStatus(status);
    }

    public List<PurchaseOrder> getOverdueOrders() {
        return purchaseOrderRepository.findByExpectedDeliveryDateBefore(LocalDate.now());
    }

    public List<PurchaseOrder> searchPurchaseOrders(String keyword) {
        return purchaseOrderRepository.findByKeyword(keyword);
    }

    public Long countByStatus(OrderStatus status) {
        return purchaseOrderRepository.countByStatus(status);
    }

    public Long countByCreatedAtBetween(LocalDate startDate, LocalDate endDate) {
        return purchaseOrderRepository.countByCreatedAtBetween(startDate, endDate);
    }

    public void deletePurchaseOrder(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with id: " + id));
        purchaseOrderRepository.delete(purchaseOrder);
    }

    private String generatePONumber() {
        // Generate PO number with format PO-YYYYMMDD-XXXX
        String datePrefix = "PO-" + LocalDate.now().toString().replace("-", "");
        Long count = purchaseOrderRepository.count() + 1;
        return datePrefix + "-" + String.format("%04d", count);
    }
}