package com.rota.facil.notification_service.messaging.consumers;

import com.rota.facil.notification_service.business.EmailService;
import com.rota.facil.notification_service.business.NotificationService;
import com.rota.facil.notification_service.messaging.dto.receive.transport.TransportTripCancelledEventReceive;
import com.rota.facil.notification_service.messaging.dto.receive.transport.TransportTripRunningEventReceive;
import com.rota.facil.notification_service.messaging.dto.receive.transport.TransportUserFeedbackRecieve;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitTransportEventConsumer {
  private final EmailService emailService;
  private final NotificationService notificationService;

  @RabbitListener(queues = "${rabbitmq.notification.trip.cancelled.queue}")
  public void handlerTripCancelled(TransportTripCancelledEventReceive event) {
    try {
      int subscribers = event.studentInfo() != null ? event.studentInfo().size() : 0;
      log.info("Iniciando processamento de viagem cancelada. tripId={} subscribers={}", event.tripId(), subscribers);
      emailService.sendEmailTripCancelled(event);
      notificationService.registerTripCancelled(event);
      log.info("Finalizado processamento de viagem cancelada. tripId={}", event.tripId());
    } catch (Exception exception) {
      log.error("Erro ao processar evento de viagem cancelada. tripId={}. Evento sera descartado para evitar reprocessamento em loop.", event != null ? event.tripId() : null, exception);
    }
  }

  @RabbitListener(queues = "${rabbitmq.notification.trip.running.queue}")
  public void handlerTripRunning(TransportTripRunningEventReceive event) {
    try {
      log.info("Registrando notificacoes de viagem iniciada. subscribers={}", event.studentInfo() != null ? event.studentInfo().size() : 0);
      notificationService.registerTripRunning(event);
    } catch (Exception exception) {
      log.error("Erro ao processar evento de viagem iniciada. tripId={}. Evento sera descartado para evitar reprocessamento em loop.", event != null ? event.tripId() : null, exception);
    }
  }

  @RabbitListener(queues = "${rabbitmq.notification.user.feedback.queue}")
  public void handleUserFeedback(TransportUserFeedbackRecieve event) {
    String subject = "Você recebeu um novo feedback - Rota Fácil";
    String templatePath = "emails/feedback-received";

    if (event.receiver() == null || event.receiver().isEmpty()) {
      log.warn("Evento de feedback recebido sem reciever");
      return;
    }

    Map<String, Object> templateVariables = Map.of(
      "sender", event.sender(),
      "receiver", event.receiver(),
      "feedback", event.feedback(),
      "note", event.note()
    );

    log.info("Iniciando envio de email de feedback. reciever={} sender={}", event.receiver(), event.sender());
    emailService.sendEmail(event.receiver(), subject, templatePath, templateVariables);
    log.info("Finalizado processamento do envio de email do evento de feedback.");
  }
}
