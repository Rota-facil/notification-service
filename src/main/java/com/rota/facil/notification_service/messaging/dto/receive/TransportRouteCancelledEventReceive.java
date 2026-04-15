package com.rota.facil.notification_service.messaging.dto.receive;

import java.util.List;

public record TransportRouteCancelledEventReceive(
        List<String> emails
) {
}
