package com.rota.facil.notification_service.messaging.dto.receive.transport;

import java.util.List;

public record TransportRouteCancelledEventReceive(
  TransportRouteDTO route,
  List<TransportSubcriberDTO> subscribers
) {}
