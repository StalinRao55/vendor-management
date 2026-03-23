package com.vms.vendor_management.service;

import com.vms.vendor_management.model.Notification;
import com.vms.vendor_management.model.User;
import com.vms.vendor_management.model.enums.NotificationType;
import com.vms.vendor_management.repository.NotificationRepository;
import com.vms.vendor_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    public Notification createNotification(Long userId, NotificationType type, String title, String message, String actionUrl, Boolean important) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setActionUrl(actionUrl);
        notification.setImportant(important != null ? important : false);
        notification.setRead(false);

        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Notification> getUnreadNotificationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return notificationRepository.findUnreadByUserOrderByCreatedAtDesc(user);
    }

    public Long countUnreadNotificationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return notificationRepository.countUnreadByUser(user);
    }

    public List<Notification> getNotificationsByUserAndType(Long userId, NotificationType type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return notificationRepository.findByUserAndTypeOrderByCreatedAtDesc(user, type);
    }

    public List<Notification> getImportantNotificationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return notificationRepository.findImportantByUserOrderByCreatedAtDesc(user);
    }

    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        notification.markAsRead();
        return notificationRepository.save(notification);
    }

    public void markAllAsReadByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        notificationRepository.markAllAsReadByUser(user, LocalDateTime.now());
    }

    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
        notificationRepository.delete(notification);
    }

    public void deleteAllNotificationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        notificationRepository.deleteAll(notifications);
    }

    // Convenience methods for common notification types
    public Notification createPOCreatedNotification(Long userId, String poNumber) {
        return createNotification(userId, NotificationType.PO_CREATED,
                "New Purchase Order Created",
                "Purchase Order " + poNumber + " has been created.",
                "/purchase-orders/" + poNumber,
                true);
    }

    public Notification createPOApprovedNotification(Long userId, String poNumber) {
        return createNotification(userId, NotificationType.PO_APPROVED,
                "Purchase Order Approved",
                "Purchase Order " + poNumber + " has been approved.",
                "/purchase-orders/" + poNumber,
                true);
    }

    public Notification createInvoiceSubmittedNotification(Long userId, String invoiceNumber) {
        return createNotification(userId, NotificationType.INVOICE_SUBMITTED,
                "Invoice Submitted",
                "Invoice " + invoiceNumber + " has been submitted for approval.",
                "/invoices/" + invoiceNumber,
                true);
    }

    public Notification createPaymentCompletedNotification(Long userId, String invoiceNumber) {
        return createNotification(userId, NotificationType.PAYMENT_COMPLETED,
                "Payment Completed",
                "Payment for Invoice " + invoiceNumber + " has been completed.",
                "/invoices/" + invoiceNumber,
                false);
    }

    public Notification createContractExpiryNotification(Long userId, String contractName, String daysLeft) {
        return createNotification(userId, NotificationType.CONTRACT_EXPIRY,
                "Contract Expiring Soon",
                "Contract " + contractName + " expires in " + daysLeft + ".",
                "/contracts",
                true);
    }
}