package com.rota.facil.notification_service.business;

import com.rota.facil.notification_service.domain.enums.*;
import com.rota.facil.notification_service.http.dto.request.user.CurrentUser;
import com.rota.facil.notification_service.http.dto.response.notification.NotificationResponseDTO;
import com.rota.facil.notification_service.messaging.dto.receive.transport.TransportTripCancelledEventReceive;
import com.rota.facil.notification_service.messaging.dto.receive.transport.TransportTripRunningEventReceive;
import com.rota.facil.notification_service.persistence.entities.NotificationEntity;
import com.rota.facil.notification_service.persistence.mappers.NotificationMapper;
import com.rota.facil.notification_service.persistence.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public void registerTripCancelled(TransportTripCancelledEventReceive event) {
        this.registerStudentsNotification(event);
        this.registerDriverNotifications(event);
        this.registerPrefectureNotifications(event);
    }

    public void registerTripRunning(TransportTripRunningEventReceive event) {
        this.registerRunningStudentsNotification(event);
        this.registerRunningDriverNotification(event);
        this.registerRunningPrefectureNotification(event);
    }

    public Page<NotificationResponseDTO> listMyNotifications(Pageable pageable, CurrentUser currentUser) {
        boolean commonUser = currentUser.role().equals(Role.DRIVER.name()) || currentUser.role().equals(Role.STUDENT.name());
        return notificationRepository.findAllByRecipientId((commonUser) ? currentUser.userId() : currentUser.prefectureId(), pageable)
                .map(notificationMapper::map);
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

    private void registerRunningStudentsNotification(TransportTripRunningEventReceive event) {
        if (event.studentInfo() == null || event.studentInfo().isEmpty()) return;
        List<NotificationEntity> studentsNotifications = event.studentInfo()
                .stream()
                .map(student -> NotificationEntity.builder()
                        .recipientType(RecipientType.STUDENT)
                        .recipientId(student.id())
                        .notificationType(NotificationType.TRIP_STARTED)
                        .priority(Priority.MEDIUM)
                        .title("Viagem " + event.routeName() + " iniciada!")
                        .message("Sua viagem " + event.routeName() + " foi iniciada.")
                        .targetId(event.tripId())
                        .targetType(TargetType.TRIP)
                        .build())
                .toList();
        notificationRepository.saveAll(studentsNotifications);
    }

    private void registerRunningDriverNotification(TransportTripRunningEventReceive event) {
        NotificationEntity driverNotification = NotificationEntity.builder()
                .recipientType(RecipientType.DRIVER)
                .recipientId(event.driverId())
                .notificationType(NotificationType.TRIP_STARTED)
                .priority(Priority.MEDIUM)
                .title("Viagem " + event.routeName() + " iniciada!")
                .message("Você iniciou a viagem " + event.routeName())
                .targetId(event.tripId())
                .targetType(TargetType.TRIP)
                .build();
        notificationRepository.save(driverNotification);
    }

    private void registerRunningPrefectureNotification(TransportTripRunningEventReceive event) {
        NotificationEntity prefectureNotification = NotificationEntity.builder()
                .recipientType(RecipientType.PREFECTURE)
                .recipientId(event.prefectureId())
                .notificationType(NotificationType.TRIP_STARTED)
                .priority(Priority.MEDIUM)
                .title("Viagem " + event.routeName() + " iniciada!")
                .message(event.driverEmail() + " iniciou a viagem " + event.routeName())
                .targetId(event.tripId())
                .targetType(TargetType.TRIP)
                .build();
        notificationRepository.save(prefectureNotification);
    }
}
