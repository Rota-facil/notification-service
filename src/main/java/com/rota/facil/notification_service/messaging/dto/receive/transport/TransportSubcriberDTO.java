package com.rota.facil.notification_service.messaging.dto.receive.transport;

import java.util.UUID;

public record TransportSubcriberDTO(
        UUID id,
        String name,
        String email
) {}
