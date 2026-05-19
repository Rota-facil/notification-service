package com.rota.facil.notification_service.messaging.consumers;

import com.rota.facil.notification_service.business.EmailService;
import com.rota.facil.notification_service.messaging.dto.receive.auth.AuthUserEventReceive;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitAuthEventConsumer {

  public final EmailService emailService;

  @RabbitListener(queues = "${rabbitmq.notification.user.created.queue}")
  public void handleUserCreated(AuthUserEventReceive authUserEventReceive) {
    String userEmail = authUserEventReceive.email();
    String subject = "Bem-vindo à Rota Fácil!";
    String templatePath = "emails/created-account";

    if (userEmail.isEmpty() || userEmail == null) {
      log.warn("Evento de usuário criado recebido sem um email do usuário");
      return;
    }

    Map<String, Object> templateVariables = Map.of(
      "email",
      userEmail,
      "loginUrl",
      "https://github.com/Rota-facil"
    );

    log.info("Iniciando envio de email de usuário criado. email={}", userEmail);

    emailService.sendEmail(userEmail, subject, templatePath, templateVariables);

    log.info(
      "Finalizado processamento do envio de email do evento de usuário criado."
    );
  }
}
