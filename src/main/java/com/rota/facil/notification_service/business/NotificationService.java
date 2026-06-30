package com.rota.facil.notification_service.business;

import com.rota.facil.notification_service.domain.enums.NotificationType;
import com.rota.facil.notification_service.domain.enums.Priority;
import com.rota.facil.notification_service.domain.enums.RecipientType;
import com.rota.facil.notification_service.domain.enums.TargetType;
import com.rota.facil.notification_service.messaging.dto.receive.transport.TransportTripCancelledEventReceive;
import com.rota.facil.notification_service.persistence.entities.NotificationEntity;
import com.rota.facil.notification_service.persistence.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void registerTripCancelled(TransportTripCancelledEventReceive event) {
        this.registerStudentsNotification(event);
        this.registerDriverNotifications(event);
        this.registerPrefectureNotifications(event);
    }

    private void registerStudentsNotification(TransportTripCancelledEventReceive event) {
        if (event.studentInfo() == null || event.studentInfo().isEmpty()) return;
        List<NotificationEntity> studentsNotifications = event.studentInfo()
                .stream()
                .map(student -> NotificationEntity.builder()
                        .recipientType(RecipientType.STUDENT)
                        .recipientId(student.id())
                        .notificationType(NotificationType.TRIP_CANCELLED)
                        .priority(Priority.HIGH)
                        .title("Viagem " + event.routeName() + " cancelada!")
                        .message("Motivo: " + event.reasonOfCancellation())
                        .targetId(event.tripId())
                        .targetType(TargetType.TRIP)
                        .build())
                .toList();
        notificationRepository.saveAll(studentsNotifications);
    }

    private void registerDriverNotifications(TransportTripCancelledEventReceive event) {
        NotificationEntity driverNotification = NotificationEntity.builder()
                .recipientType(RecipientType.DRIVER)
                .recipientId(event.driverId())
                .notificationType(NotificationType.TRIP_CANCELLED)
                .priority(Priority.HIGH)
                .title("Viagem " + event.routeName() + " cancelada!")
                .message("Você cancelou a viagem " + event.routeName())
                .targetId(event.tripId())
                .targetType(TargetType.TRIP)
                .build();
        notificationRepository.save(driverNotification);
    }

    private void registerPrefectureNotifications(TransportTripCancelledEventReceive event) {
        NotificationEntity prefectureNotification = NotificationEntity.builder()
                .recipientType(RecipientType.PREFECTURE)
                .recipientId(event.prefectureId())
                .notificationType(NotificationType.TRIP_CANCELLED)
                .priority(Priority.HIGH)
                .title("Viagem " + event.routeName() + " cancelada!")
                .message(event.driverEmail() + " cancelou sua viagem pelo motivo: " + event.reasonOfCancellation())
                .targetId(event.tripId())
                .targetType(TargetType.TRIP)
                .build();
        notificationRepository.save(prefectureNotification);
    }
}
