package com.rota.facil.notification_service.business;

import com.rota.facil.notification_service.messaging.dto.receive.transport.TransportTripCancelledEventReceive;
import com.rota.facil.notification_service.messaging.mappers.TripCancelledTemplateVariablesMapper;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;
  private final EmailTemplateRender emailTemplateRender;


  public void sendEmailTripCancelled(TransportTripCancelledEventReceive event) {
    String subjectEmail = "A sua viagem foi cancelada";
    String templatePath = "emails/trip-cancelled";

        event
            .studentInfo()
            .stream()
            .filter(sub -> !sub.email().isBlank() && !sub.name().isBlank())
            .forEach(sub -> {

              this.sendEmail(
                      sub.email(),
                      subjectEmail,
                      templatePath,
                      Map.of("name", sub.name())
              );
            });

    log.info(
            "Finalizado processamento do envio de emails do evento de rotas canceladas"
    );
  }

  public void sendEmail(
    String to,
    String subject,
    String templatePath,
    Map<String, Object> variables
  ) {
    String emailTemplate = emailTemplateRender.render(templatePath, variables);

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setTo(to);
      helper.setSubject(subject);
      helper.setFrom("noreply@rotafacil.com");
      helper.setText(emailTemplate, true);

      mailSender.send(message);
    } catch (Exception e) {
      log.error("Erro ao enviar email para={}", to, e);
      e.printStackTrace();
    }
  }
}
