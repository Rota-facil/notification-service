package com.rota.facil.notification_service.messaging.mappers;

import com.rota.facil.notification_service.messaging.dto.receive.transport.TransportRouteCancelledEventReceive;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TripCancelledTemplateVariablesMapper {

  public final Map<String, Object> createBaseVariables(
    TransportRouteCancelledEventReceive event
  ) {
    var route = event.route();

    Map<String, Object> variables = new HashMap<>();

    variables.put("origin", route.prefectureName());
    variables.put("destination", route.goingTo());
    variables.put("date", route.date());
    variables.put("reason", route.reassonOfCancelled());

    return variables;
  }
}
