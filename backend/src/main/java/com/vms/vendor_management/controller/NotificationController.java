package com.vms.vendor_management.controller;

import com.vms.vendor_management.model.Notification;
import com.vms.vendor_management.model.enums.NotificationType;
import com.vms.vendor_management.service.NotificationService;
import com.vms.vendor_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'VENDOR')")
    public List<Notification> getNotificationsByUser(@PathVariable Long userId) {
        return notificationService.getNotificationsByUser(userId);
    }

    @GetMapping("/user/{userId}/unread")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'VENDOR')")
    public List<Notification> getUnreadNotificationsByUser(@PathVariable Long userId) {
        return notificationService.getUnreadNotificationsByUser(userId);
    }

    @GetMapping("/user/{userId}/unread/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'VENDOR')")
    public Long getUnreadCountByUser(@PathVariable Long userId) {
        return notificationService.countUnreadNotificationsByUser(userId);
    }

    @GetMapping("/user/{userId}/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'VENDOR')")
    public List<Notification> getNotificationsByUserAndType(@PathVariable Long userId,
                                                          @PathVariable NotificationType type) {
        return notificationService.getNotificationsByUserAndType(userId, type);
    }

    @GetMapping("/user/{userId}/important")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'VENDOR')")
    public List<Notification> getImportantNotificationsByUser(@PathVariable Long userId) {
        return notificationService.getImportantNotificationsByUser(userId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'VENDOR')")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        // Note: This endpoint would need additional logic to ensure users can only access their own notifications
        // For now, it's a basic implementation
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/mark-read")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'VENDOR')")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        Notification notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/user/{userId}/mark-all-read")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'VENDOR')")
    public ResponseEntity<Map<String, Boolean>> markAllAsReadByUser(@PathVariable Long userId) {
        notificationService.markAllAsReadByUser(userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("markedAsRead", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'VENDOR')")
    public ResponseEntity<Map<String, Boolean>> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/user/{userId}/delete-all")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM', 'VENDOR')")
    public ResponseEntity<Map<String, Boolean>> deleteAllNotificationsByUser(@PathVariable Long userId) {
        notificationService.deleteAllNotificationsByUser(userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deletedAll", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    // Admin endpoints for creating system notifications
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER')")
    public ResponseEntity<Notification> createNotification(@RequestParam Long userId,
                                                         @RequestParam NotificationType type,
                                                         @RequestParam String title,
                                                         @RequestParam String message,
                                                         @RequestParam(required = false) String actionUrl,
                                                         @RequestParam(required = false) Boolean important) {
        Notification notification = notificationService.createNotification(userId, type, title, message, actionUrl, important);
        return ResponseEntity.ok(notification);
    }

    // Convenience endpoints for common notification types
    @PostMapping("/po-created")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER')")
    public ResponseEntity<Notification> createPOCreatedNotification(@RequestParam Long userId,
                                                                  @RequestParam String poNumber) {
        Notification notification = notificationService.createPOCreatedNotification(userId, poNumber);
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/po-approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER')")
    public ResponseEntity<Notification> createPOApprovedNotification(@RequestParam Long userId,
                                                                   @RequestParam String poNumber) {
        Notification notification = notificationService.createPOApprovedNotification(userId, poNumber);
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/invoice-submitted")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER', 'FINANCE_TEAM')")
    public ResponseEntity<Notification> createInvoiceSubmittedNotification(@RequestParam Long userId,
                                                                         @RequestParam String invoiceNumber) {
        Notification notification = notificationService.createInvoiceSubmittedNotification(userId, invoiceNumber);
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/payment-completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_TEAM')")
    public ResponseEntity<Notification> createPaymentCompletedNotification(@RequestParam Long userId,
                                                                         @RequestParam String invoiceNumber) {
        Notification notification = notificationService.createPaymentCompletedNotification(userId, invoiceNumber);
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/contract-expiry")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_MANAGER')")
    public ResponseEntity<Notification> createContractExpiryNotification(@RequestParam Long userId,
                                                                       @RequestParam String contractName,
                                                                       @RequestParam String daysLeft) {
        Notification notification = notificationService.createContractExpiryNotification(userId, contractName, daysLeft);
        return ResponseEntity.ok(notification);
    }
}