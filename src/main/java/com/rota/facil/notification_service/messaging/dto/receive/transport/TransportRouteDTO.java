package com.rota.facil.notification_service.messaging.dto.receive.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record TransportRouteDTO(
  @JsonProperty("reasson_of_cancelled") String reassonOfCancelled,
  @JsonProperty("prefecture_name") String prefectureName,
  @JsonProperty("goin_to") String goingTo,
  LocalDateTime date
) {}
