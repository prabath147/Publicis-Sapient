package com.pharmacy.service.client.notify;

import com.pharmacy.service.dtoexternal.UserNotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "notification",
        url = "${notifyServiceURL}/notification"
)
public interface NotificationClient {
    @PostMapping("/create-notification")
    ResponseEntity<UserNotificationDto> createUserNotification(@RequestBody UserNotificationDto userNotificationRequest);

    @PostMapping("/create-notifications")
    ResponseEntity<List<UserNotificationDto>> createUserNotification(@RequestBody List<UserNotificationDto> userNotificationRequestList);

    @PostMapping("/create-notification")
    ResponseEntity<UserNotificationDto> createUserNotification(@RequestHeader("Authorization") String token, @RequestBody UserNotificationDto userNotificationRequest);

    @PostMapping("/create-notifications")
    ResponseEntity<List<UserNotificationDto>> createUserNotification(@RequestHeader("Authorization") String token, @RequestBody List<UserNotificationDto> userNotificationRequestList);
}
