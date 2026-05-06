package com.rota.facil.notification_service.business;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailTemplateRender {

  private final TemplateEngine templateEngine;

  public String render(String templateName, Map<String, Object> variables) {
    Context context = new Context();
    context.setVariables(variables);

    return templateEngine.process(templateName, context);
  }
}
