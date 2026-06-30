package com.rota.facil.notification_service.messaging.mappers;

import com.rota.facil.notification_service.messaging.dto.receive.transport.TransportTripCancelledEventReceive;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TripCancelledTemplateVariablesMapper {

  public final Map<String, Object> createBaseVariables(
    TransportTripCancelledEventReceive event
  ) {
//    var route = event.route();
//
//    Map<String, Object> variables = new HashMap<>();
//
//    variables.put("origin", route.prefectureName());
//    variables.put("destination", route.goingTo());
//    variables.put("date", route.date());
//    variables.put("reason", route.reassonOfCancelled());
//
//    return variables;
    return null;
  }
}
