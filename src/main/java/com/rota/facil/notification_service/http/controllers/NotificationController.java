package com.rota.facil.notification_service.http.controllers;

import com.rota.facil.notification_service.business.NotificationService;
import com.rota.facil.notification_service.http.dto.request.user.CurrentUser;
import com.rota.facil.notification_service.http.dto.response.notification.NotificationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/my")
    public ResponseEntity<Page<NotificationResponseDTO>> listMyNotifications(
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal CurrentUser currentUser
    ) {
            return ResponseEntity.ok(notificationService.listMyNotifications(pageable, currentUser));
    }
}
