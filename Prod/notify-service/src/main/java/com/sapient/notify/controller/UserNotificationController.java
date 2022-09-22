package com.sapient.notify.controller;

import com.sapient.notify.dto.PageableResponse;
import com.sapient.notify.dto.UserNotificationDto;
import com.sapient.notify.service.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")

@RestController
@RequestMapping("/notify/notification")
public class UserNotificationController {

    @Autowired
    private UserNotificationService userNotificationService;

    @PostMapping("/create-notification")
    public ResponseEntity<UserNotificationDto> createUserNotification(
            @Valid @RequestBody UserNotificationDto userNotificationRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userNotificationService.createUserNotification(userNotificationRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create-notifications")
    public ResponseEntity<List<UserNotificationDto>> createUserNotification(
            @Valid @RequestBody List<UserNotificationDto> userNotificationRequestList) {
        try {
            List<UserNotificationDto> userNotificationResponseList = new ArrayList<>();
            for (UserNotificationDto ur : userNotificationRequestList)
                userNotificationResponseList.add(userNotificationService.createUserNotification(ur));
            return ResponseEntity.status(HttpStatus.CREATED).body(userNotificationResponseList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PageableResponse<UserNotificationDto>> getUserNotifications(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "ascending", required = false) String sortOrder,
            @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userNotificationService.getUserNotifications(userId,pageNumber,pageSize,sortBy,sortOrder));
    }

    @PutMapping("/toggle-status/{notificationId}")
    public ResponseEntity<UserNotificationDto> toggleNotificationStatus(@PathVariable Long notificationId) {
        try {
            return ResponseEntity.ok().body(userNotificationService.toggleNotificationStatus(notificationId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
}
