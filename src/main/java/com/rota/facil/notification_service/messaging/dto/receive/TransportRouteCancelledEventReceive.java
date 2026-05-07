package com.rota.facil.notification_service.messaging.dto.receive;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public record TransportRouteCancelledEventReceive(
  RouteDto route,
  List<SubscriberDto> subscribers
) {
  public record RouteDto(
    @JsonProperty("reasson_of_cancelled") String reassonOfCancelled,
    @JsonProperty("prefecture_name") String prefectureName,
    @JsonProperty("goin_to") String goingTo,
    LocalDateTime date
  ) {}

  public record SubscriberDto(String name, String email) {}
}
