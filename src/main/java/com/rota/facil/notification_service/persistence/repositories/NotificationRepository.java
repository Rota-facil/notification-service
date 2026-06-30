package com.rota.facil.notification_service.persistence.repositories;

import com.rota.facil.notification_service.http.dto.response.notification.NotificationResponseDTO;
import com.rota.facil.notification_service.persistence.entities.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {
    @Query("""
        SELECT n FROM NotificationEntity n
        WHERE n.recipientId = :recipientId
        ORDER BY n.createdAt DESC
    """)
    Page<NotificationEntity> findAllByRecipientId(@Param("recipientId") UUID recipientId, Pageable pageable);
}
