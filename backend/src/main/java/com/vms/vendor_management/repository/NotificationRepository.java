package com.vms.vendor_management.repository;

import com.vms.vendor_management.model.Notification;
import com.vms.vendor_management.model.User;
import com.vms.vendor_management.model.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    List<Notification> findByUserAndReadOrderByCreatedAtDesc(User user, Boolean read);

    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.read = false ORDER BY n.createdAt DESC")
    List<Notification> findUnreadByUserOrderByCreatedAtDesc(@Param("user") User user);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user = :user AND n.read = false")
    Long countUnreadByUser(@Param("user") User user);

    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.type = :type ORDER BY n.createdAt DESC")
    List<Notification> findByUserAndTypeOrderByCreatedAtDesc(@Param("user") User user, @Param("type") NotificationType type);

    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.createdAt >= :since ORDER BY n.createdAt DESC")
    List<Notification> findByUserSinceOrderByCreatedAtDesc(@Param("user") User user, @Param("since") LocalDateTime since);

    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.important = true ORDER BY n.createdAt DESC")
    List<Notification> findImportantByUserOrderByCreatedAtDesc(@Param("user") User user);

    Page<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    @Query("UPDATE Notification n SET n.read = true, n.readAt = :readAt WHERE n.user = :user AND n.read = false")
    void markAllAsReadByUser(@Param("user") User user, @Param("readAt") LocalDateTime readAt);
}