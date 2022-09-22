package com.sapient.notify.service.implementation;

import com.sapient.notify.dto.PageableResponse;
import com.sapient.notify.dto.UserNotificationDto;
import com.sapient.notify.exception.ResourceException;
import com.sapient.notify.model.NotificationSeverity;
import com.sapient.notify.model.NotificationStatus;
import com.sapient.notify.model.UserNotification;
import com.sapient.notify.repository.UserNotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserNotificationServiceImplTest {

    @InjectMocks
    UserNotificationServiceImpl userNotificationService;

    @Mock
    private UserNotificationRepository userNotificationRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private ModelMapper modelMapper;

    List<UserNotification> userNotifications;
    List<UserNotificationDto> userNotificationDtoList;

    Page<UserNotification> mockNotificationPage;

    private static final String RESOURCE_NAME = "UserNotification";

    @BeforeEach
    void setup() throws Exception{
        MockitoAnnotations.openMocks(this);

        // mock data
        userNotifications = new ArrayList<UserNotification>();
        userNotificationDtoList = new ArrayList<UserNotificationDto>();
        userNotifications.add(UserNotification.builder()
                .id(7L)
                .message("sample message 1")
                .description("sample description 1")
                .status(NotificationStatus.SEEN)
                .severity(NotificationSeverity.WARNING)
                .createdOn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-01-01 10:10:10"))
                .userId(1L)
                .build());

        userNotifications.add(UserNotification.builder()
                .id(8L)
                .message("sample message 2")
                .description("sample description 2")
                .status(NotificationStatus.UNSEEN)
                .severity(NotificationSeverity.ERROR)
                .createdOn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-01-01 10:10:10"))
                .userId(1L)
                .build());

        userNotificationDtoList.add(UserNotificationDto.builder()
                .id(7L)
                .message("sample message 1")
                .description("sample description 1")
                .status(NotificationStatus.SEEN)
                .severity(NotificationSeverity.WARNING)
                .createdOn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-01-01 10:10:10"))
                .userId(1L)
                .build());

        userNotificationDtoList.add(UserNotificationDto.builder()
                .id(8L)
                .message("sample message 2")
                .description("sample description 2")
                .status(NotificationStatus.UNSEEN)
                .severity(NotificationSeverity.ERROR)
                .createdOn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-01-01 10:10:10"))
                .userId(1L)
                .build());

        mockNotificationPage = new PageImpl<>(userNotifications);
    }

    @Test
    void createUserNotification() throws Exception {
        // create a userNotification Request
        when(userNotificationRepository.save(any(UserNotification.class))).thenReturn(userNotifications.get(0));
        lenient().doNothing().when(messagingTemplate).convertAndSend(anyString(),any(UserNotification.class));
        when(modelMapper.map(any(UserNotification.class), any())).thenReturn(userNotificationDtoList.get(0));
        when(modelMapper.map(any(UserNotificationDto.class), any())).thenReturn(userNotifications.get(0));
        // validate response of user notification service
        UserNotificationDto userNotificationDto = userNotificationService.createUserNotification(userNotificationDtoList.get(0));
        assertEquals("sample message 1",userNotificationDto.getMessage());
    }

    @Test
    void createUserNotificationThrowsException() throws Exception {
        UserNotificationDto mockData = userNotificationDtoList.get(0);
        when(userNotificationRepository.save(any(UserNotification.class))).thenThrow(new DataAccessResourceFailureException("e"));
        assertThatThrownBy(() -> userNotificationService.createUserNotification(mockData)).isInstanceOf(ResourceException.class);
    }

    @Test
    void getUserNotifications_Ascending(){
        // mock data
        when(userNotificationRepository.findAllByUserId(anyLong(), any())).thenReturn(mockNotificationPage);
        when(modelMapper.map(any(UserNotification.class), any())).thenReturn(userNotificationDtoList.get(0)).thenReturn(userNotificationDtoList.get(1));
        PageableResponse<UserNotificationDto> expected = new PageableResponse<>();
        expected.setResponseData(userNotificationDtoList,mockNotificationPage);

        PageableResponse<UserNotificationDto> actual = userNotificationService.getUserNotifications(1L,0,10,"id","ascending");
        assertEquals(expected.getData(),actual.getData());
    }

    @Test
    void getUserNotifications_Descending(){
        // mock data
        when(userNotificationRepository.findAllByUserId(anyLong(), any())).thenReturn(mockNotificationPage);
        when(modelMapper.map(any(UserNotification.class), any())).thenReturn(userNotificationDtoList.get(1)).thenReturn(userNotificationDtoList.get(0));
        PageableResponse<UserNotificationDto> expected = new PageableResponse<>();
        Collections.reverse(userNotificationDtoList);
        expected.setResponseData(userNotificationDtoList,mockNotificationPage);

        PageableResponse<UserNotificationDto> actual = userNotificationService.getUserNotifications(1L,0,10,"id","descending");
        assertEquals(expected.getData(),actual.getData());
    }

    @Test
    void toggleNotificationStatus_isEmpty() {
        when(userNotificationRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userNotificationService.toggleNotificationStatus(1L)).isInstanceOf(ResourceException.class);
    }

    @Test
    void toggleNotificationStatusThrowsException() throws Exception {
        when(userNotificationRepository.findById(anyLong())).thenReturn(Optional.of(userNotifications.get(0)));
        when(userNotificationRepository.save(any(UserNotification.class))).thenThrow(new DataAccessResourceFailureException("e"));
        assertThatThrownBy(() -> userNotificationService.toggleNotificationStatus(7L)).isInstanceOf(ResourceException.class);
    }

    @Test
    void toggleNotificationStatus_SEEN() throws Exception{
        // mock the data
        UserNotificationDto result = UserNotificationDto.builder()
                .id(7L)
                .message("sample message 1")
                .description("sample description 1")
                .status(NotificationStatus.UNSEEN)
                .severity(NotificationSeverity.WARNING)
                .createdOn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-01-01 10:10:10"))
                .userId(1L)
                .build();

        when(userNotificationRepository.findById(anyLong())).thenReturn(Optional.of(userNotifications.get(0)));
        when(modelMapper.map(any(), any())).thenReturn(result);

        // validate the service implementation
        UserNotificationDto userNotificationDto = userNotificationService.toggleNotificationStatus(7L);
        assertEquals(NotificationStatus.UNSEEN,userNotificationDto.getStatus());
    }

    @Test
    void toggleNotificationStatus_UNSEEN() throws Exception{
        // mock the data
        UserNotificationDto result = UserNotificationDto.builder()
                .id(8L)
                .message("sample message 2")
                .description("sample description 2")
                .status(NotificationStatus.SEEN)
                .severity(NotificationSeverity.ERROR)
                .createdOn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-01-01 10:10:10"))
                .userId(1L)
                .build();

        when(userNotificationRepository.findById(anyLong())).thenReturn(Optional.of(userNotifications.get(1)));
        when(modelMapper.map(any(), any())).thenReturn(result);

        // validate the service implementation
        UserNotificationDto userNotificationDto = userNotificationService.toggleNotificationStatus(8L);
        assertEquals(NotificationStatus.SEEN,userNotificationDto.getStatus());
    }
}