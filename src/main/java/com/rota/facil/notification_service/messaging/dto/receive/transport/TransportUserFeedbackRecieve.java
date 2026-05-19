package com.rota.facil.notification_service.messaging.dto.receive.transport;

public record TransportUserFeedbackRecieve(
  String sender,
  String receiver,
  String feedback,
  Double note
) {}
