package com.rota.facil.notification_service.http.dto.response.notification;

import com.rota.facil.notification_service.domain.enums.NotificationType;
import com.rota.facil.notification_service.domain.enums.Priority;
import com.rota.facil.notification_service.domain.enums.RecipientType;
import com.rota.facil.notification_service.domain.enums.TargetType;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponseDTO(
        UUID id,
        UUID recipientId,
        UUID targetId,
        RecipientType recipientType,
        NotificationType notificationType,
        String title,
        String message,
        TargetType targetType,
        Priority priority,
        LocalDateTime createdAt 
) {
}
