package com.sapient.notify.service;

import com.sapient.notify.dto.PageableResponse;
import com.sapient.notify.dto.UserNotificationDto;

public interface UserNotificationService {
    public UserNotificationDto createUserNotification(UserNotificationDto userNotificationRequest);
    public PageableResponse<UserNotificationDto> getUserNotifications(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    public UserNotificationDto toggleNotificationStatus(Long notificationId);
}
