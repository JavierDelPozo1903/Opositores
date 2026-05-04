package com.opositores.repository;

import com.opositores.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdAndSentAtIsNull(Long userId);

    /** Notificaciones programadas pendientes de enviar */
    List<Notification> findByScheduledAtBeforeAndSentAtIsNull(LocalDateTime now);
}
