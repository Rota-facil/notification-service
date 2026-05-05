package com.rota.facil.notification_service.messaging.consumers;

import com.rota.facil.notification_service.messaging.dto.receive.TransportRouteCancelledEventReceive;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitTransportEventConsumer {

  // injeta um EmailService para fazer as operacoes de lógica de negócio para envio de email

  @RabbitListener(queues = "${rabbitmq.notification.trip.cancelled.queue}")
  public void handlerRouteCancelled(
    TransportRouteCancelledEventReceive transportRouteCancelledEventReceive
  ) {
    // chama service para enviar os email de cancelamento de viagem
  }
}
