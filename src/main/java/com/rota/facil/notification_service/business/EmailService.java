package com.rota.facil.notification_service.business;

import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  @Autowired
  private final JavaMailSender mailSender;

  private final EmailTemplateRender emailTemplateRender;

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
      log.error("Erro ao enviar email para={}", variables.get("name"), e);
      e.printStackTrace();
    }
  }
}
