package com.order.service.client.notify;

import com.order.service.dtoexternal.UserNotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}
