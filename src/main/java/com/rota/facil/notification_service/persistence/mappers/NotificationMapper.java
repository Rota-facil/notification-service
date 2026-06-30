package com.rota.facil.notification_service.persistence.mappers;

import com.rota.facil.notification_service.http.dto.response.notification.NotificationResponseDTO;
import com.rota.facil.notification_service.persistence.entities.NotificationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponseDTO map(NotificationEntity entity);
}
