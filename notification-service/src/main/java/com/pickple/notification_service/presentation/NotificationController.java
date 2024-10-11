package com.pickple.notification_service.presentation;

import com.pickple.common_module.presentation.dto.ApiResponse;
import com.pickple.notification_service.application.service.EmailService;
import com.pickple.notification_service.application.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


}
