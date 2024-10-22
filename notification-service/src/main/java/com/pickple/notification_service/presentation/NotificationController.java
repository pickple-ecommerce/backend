package com.pickple.notification_service.presentation;

import com.pickple.common_module.presentation.dto.ApiResponse;
import com.pickple.notification_service.application.dto.ChannelCreateRespDto;
import com.pickple.notification_service.application.dto.NotificationRespDto;
import com.pickple.notification_service.application.service.NotificationService;
import com.pickple.notification_service.presentation.request.ChannelCreateReqDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Value("${slack.workspaceUrl}")
    private String slackWorkspaceUrl;

    @PreAuthorize("hasAuthority('MASTER')")
    @DeleteMapping("/{notification_id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable("notification_id") UUID notificationId) {
        notificationService.deleteNotification(notificationId);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('USER', 'MASTER', 'VENDOR_MANAGER')")
    @GetMapping("/notification-history/{username}")
    public ResponseEntity<ApiResponse<Page<NotificationRespDto>>> notificationHistory (
            @PathVariable("username") String username,
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value="size", defaultValue = "10") int size,
            @RequestParam(value="sort", defaultValue = "createdAt, desc") String[] sort
    )
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        ApiResponse<Page<NotificationRespDto>> response = ApiResponse.success(
                HttpStatus.OK,
                "전체 알림 내역이 조회 되었습니다.",
                notificationService.notificationHistory(username, pageable)
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('MASTER')")
    @GetMapping("/get-all-notifications")
    public ResponseEntity<ApiResponse<Page<NotificationRespDto>>> getAllNotifications(
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value="size", defaultValue = "10") int size,
            @RequestParam(value="sort", defaultValue = "createdAt, desc") String[] sort
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        ApiResponse<Page<NotificationRespDto>> response = ApiResponse.success(
                HttpStatus.OK,
                "전체 알림 내역이 조회 되었습니다.",
                notificationService.getAllNotifications(pageable)
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('MASTER')")
    @PostMapping("/channel")
    public ResponseEntity<ApiResponse<ChannelCreateRespDto>> createChannel(@Valid @RequestBody ChannelCreateReqDto reqDto) {
        ApiResponse<ChannelCreateRespDto> response = ApiResponse.success(
                HttpStatus.OK,
                "전체 알림 내역이 조회 되었습니다.",
                notificationService.createChannel(reqDto)
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('MASTER')")
    @DeleteMapping("/channel/{channel_id}")
    public ResponseEntity<ApiResponse<ChannelCreateRespDto>> deleteChannel(@PathVariable("channel_id") UUID channelId) {
        notificationService.deleteChannel(channelId);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('MASTER')")
    @GetMapping("/slack-for-admin")
    public ResponseEntity<ApiResponse<String>> getSlackUrl(){
        ApiResponse<String> response = ApiResponse.success(
                HttpStatus.OK,
                "관리자용 슬랙 워크 스페이스 url 입니다.",
                slackWorkspaceUrl
        );

        return ResponseEntity.ok(response);
    }

}
