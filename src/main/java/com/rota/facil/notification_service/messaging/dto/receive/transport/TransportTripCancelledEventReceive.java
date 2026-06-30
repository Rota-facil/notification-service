package com.rota.facil.notification_service.messaging.dto.receive.transport;

import java.util.List;
import java.util.UUID;

public record TransportTripCancelledEventReceive(
    UUID driverId,
    String driverEmail,
    UUID prefectureId,
    UUID tripId,
    String routeName,
    String reasonOfCancellation,
    List<TransportSubcriberDTO> studentInfo
) {}
