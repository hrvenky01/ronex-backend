package com.ronex.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ronex.backend.model.Notification;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
}