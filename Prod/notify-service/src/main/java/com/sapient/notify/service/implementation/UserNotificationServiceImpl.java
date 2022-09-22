package com.sapient.notify.service.implementation;

import com.sapient.notify.dto.PageableResponse;
import com.sapient.notify.dto.UserNotificationDto;
import com.sapient.notify.exception.ResourceException;
import com.sapient.notify.model.NotificationStatus;
import com.sapient.notify.model.UserNotification;
import com.sapient.notify.repository.UserNotificationRepository;
import com.sapient.notify.service.UserNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserNotificationServiceImpl implements UserNotificationService {

    @Autowired
    UserNotificationRepository userNotificationRepository;

    private final SimpMessagingTemplate messagingTemplate;

    private static final String RESOURCE_NAME = "UserNotification";

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public UserNotificationServiceImpl(final SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public UserNotificationDto createUserNotification(UserNotificationDto userNotificationRequest) {
        try{
            // add notification to db
            // add validation for getUserId() - check if such a user exists
            userNotificationRequest.setStatus(NotificationStatus.UNSEEN);
            UserNotification userNotification = modelMapper.map(userNotificationRequest, UserNotification.class);
            log.info("Creating a new User Notification " + userNotification);
            UserNotification nUserNotification = userNotificationRepository.save(userNotification);
            // send response through socket
            UserNotificationDto userNotificationResponse = modelMapper.map(nUserNotification,UserNotificationDto.class);
            messagingTemplate.convertAndSend("/topic/messages/"+userNotification.getUserId(),userNotificationResponse);
            return userNotificationResponse;
        } catch (Exception e){
            throw new ResourceException(RESOURCE_NAME, "userNotification", userNotificationRequest, ResourceException.ErrorType.CREATED,e);
        }
    }

    @Override
    public PageableResponse<UserNotificationDto> getUserNotifications(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = Sort.by(sortBy);
        if(sortOrder.equals("descending"))
            sort = sort.descending();
        Pageable requestedPage = PageRequest.of(pageNumber,pageSize,sort);
        Page<UserNotification> notificationPage = userNotificationRepository.findAllByUserId(userId, requestedPage);
//        if(notificationPage.isEmpty())
//            throw new ResourceException(RESOURCE_NAME, "userId", userId, ResourceException.ErrorType.FOUND);
        List<UserNotification> notifications = notificationPage.getContent();
        List<UserNotificationDto> userNotificationDtos =  notifications
                .stream()
                .map(userNotification -> modelMapper.map(userNotification, UserNotificationDto.class))
                .collect(Collectors.toList());
        PageableResponse<UserNotificationDto> pageableResponse = new PageableResponse<>();
        return pageableResponse.setResponseData(userNotificationDtos, notificationPage);
    }

    @Override
    public UserNotificationDto toggleNotificationStatus(Long notificationId){
        Optional<UserNotification> userNotification = userNotificationRepository.findById(notificationId);
        if(userNotification.isEmpty())
            throw new ResourceException(RESOURCE_NAME, "notificationId", notificationId, ResourceException.ErrorType.FOUND);

        UserNotification nUserNotification = userNotification.get();
        if(nUserNotification.getStatus().equals(NotificationStatus.SEEN))
            nUserNotification.setStatus(NotificationStatus.UNSEEN);
        else
            nUserNotification.setStatus(NotificationStatus.SEEN);
        try{
            UserNotification savedUserNotification = userNotificationRepository.save(nUserNotification);
            return modelMapper.map(savedUserNotification, UserNotificationDto.class);
        }catch (Exception e){
            throw new ResourceException(RESOURCE_NAME, "notificationId", notificationId, ResourceException.ErrorType.UPDATED, e);
        }
    }
}
