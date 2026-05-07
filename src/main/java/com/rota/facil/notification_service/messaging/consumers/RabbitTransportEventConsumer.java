package com.rota.facil.notification_service.messaging.consumers;

import com.rota.facil.notification_service.business.EmailService;
import com.rota.facil.notification_service.messaging.dto.receive.TransportRouteCancelledEventReceive;
import com.rota.facil.notification_service.messaging.mappers.TripCancelledTemplateVariablesMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitTransportEventConsumer {

  @Autowired
  private final EmailService emailService;

  private final TripCancelledTemplateVariablesMapper tripCancelledTemplateMapper;

  @RabbitListener(queues = "${rabbitmq.notification.trip.cancelled.queue}")
  public void handlerRouteCancelled(
    TransportRouteCancelledEventReceive transportRouteCancelledEventReceive
  ) {
    String subjectEmail = "A sua viagem foi cancelada";
    String templatePath = "emails/trip-cancelled";

    if (
      transportRouteCancelledEventReceive.subscribes() == null ||
      transportRouteCancelledEventReceive.subscribes().isEmpty()
    ) {
      return;
    }

    Map<String, Object> baseVariables =
      tripCancelledTemplateMapper.createBaseVariables(
        transportRouteCancelledEventReceive
      );

    transportRouteCancelledEventReceive
      .subscribes()
      .stream()
      .filter(sub -> !sub.email().isBlank() && !sub.name().isBlank())
      .forEach(sub -> {
        baseVariables.put("name", sub.name());

        emailService.sendEmail(
          sub.email(),
          subjectEmail,
          templatePath,
          baseVariables
        );
      });
  }
}
