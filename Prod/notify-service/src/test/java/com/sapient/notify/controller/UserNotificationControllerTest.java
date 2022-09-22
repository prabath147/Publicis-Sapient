package com.sapient.notify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.notify.dto.PageableResponse;
import com.sapient.notify.dto.UserNotificationDto;
import com.sapient.notify.exception.ResourceException;
import com.sapient.notify.model.NotificationSeverity;
import com.sapient.notify.model.NotificationStatus;
import com.sapient.notify.model.UserNotification;
import com.sapient.notify.service.UserNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserNotificationControllerTest {
	@Autowired
	private UserNotificationController userNotificationController;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserNotificationService userNotificationService;

	@Autowired
	private ModelMapper modelMapper;

	private static final String RESOURCE_NAME = "UserNotification";

	List<UserNotificationDto> userNotificationDtoList;
	List<UserNotification> userNotificationList;

	@BeforeEach
	void setup() throws Exception {
		// mock necessary data for tests
		userNotificationDtoList = new ArrayList<UserNotificationDto>();
		userNotificationList = new ArrayList<UserNotification>();

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

		userNotificationList.add(UserNotification.builder()
				.id(7L)
				.message("sample message 1")
				.description("sample description 1")
				.status(NotificationStatus.SEEN)
				.severity(NotificationSeverity.WARNING)
				.createdOn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-01-01 10:10:10"))
				.userId(1L)
				.build());

		userNotificationList.add(UserNotification.builder()
				.id(8L)
				.message("sample message 2")
				.description("sample description 2")
				.status(NotificationStatus.UNSEEN)
				.severity(NotificationSeverity.ERROR)
				.createdOn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-01-01 10:10:10"))
				.userId(1L)
				.build());
	}

	@Test
	@WithMockUser(username = "user", roles = {"CUSTOMER"})
	void getUserNotifications() throws Exception {
		// mock the data returned by user notification service class
		PageableResponse<UserNotificationDto> mockPageResponse = new PageableResponse<>();
		Pageable requestedPage = PageRequest.of(0,2);
		Page<UserNotification> mockPage = new PageImpl<>(userNotificationList,requestedPage,userNotificationList.size());
		mockPageResponse.setResponseData(userNotificationDtoList,mockPage);

		when(userNotificationService.getUserNotifications(anyLong(),anyInt(), anyInt(), anyString(),anyString())).thenReturn(mockPageResponse);

		// create a mock HTTP request to verify the expected result
		mockMvc.perform(MockMvcRequestBuilders
						.get("/notify/notification/1"))
				.andExpect(status().isOk());

		// add more test cases for this
	}

	@Test
	void getUserNotificationThrowException() throws Exception {
		// mock behaviour of notification service - throws an exception
		when(userNotificationService.getUserNotifications(anyLong(),anyInt(), anyInt(), anyString(),anyString())).thenThrow(new ResourceException(
				RESOURCE_NAME, "userId", anyLong(), ResourceException.ErrorType.FOUND));

		// create a mock HTTP request to throw an exception
		mockMvc.perform(MockMvcRequestBuilders
						.get("/notify/notification/1")
						.with(SecurityMockMvcRequestPostProcessors.user("user").roles("ADMIN")))
				.andExpect(status().is5xxServerError());
	}

	@Test
	void createUserNotification() throws Exception {
		// mock the notification data to be saved
		when(userNotificationService.createUserNotification(any())).thenReturn(userNotificationDtoList.get(0));

		// mock HTTP request to create user notification
		mockMvc.perform(MockMvcRequestBuilders
						.post("/notify/notification/create-notification")
						.with(SecurityMockMvcRequestPostProcessors.user("user").roles("ADMIN"))
						.content(new ObjectMapper().writeValueAsString(userNotificationDtoList.get(0)))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
	}

	@Test
	@WithMockUser(username = "user", roles = {"USER"})
	void createUserNotificationThrowException() throws Exception {
		// mock behaviour of notification service - throws an exception
		when(userNotificationService.createUserNotification(any())).thenThrow(new EntityNotFoundException());

		// mock HTTP request to create user notification
		mockMvc.perform(MockMvcRequestBuilders.post("/notify/notification/create-notification")
				.content(new ObjectMapper().writeValueAsString(userNotificationDtoList.get(0)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

	@Test
	@WithMockUser(username = "user", roles = {"USER"})
	void createUserNotifications() throws Exception {
		// mock the notification data to be saved
		when(userNotificationService.createUserNotification(any())).thenReturn(userNotificationDtoList.get(0))
				.thenReturn(userNotificationDtoList.get(1));

		// mock HTTP request to create user notification
		mockMvc.perform(MockMvcRequestBuilders.post("/notify/notification/create-notifications")
				.content(new ObjectMapper().writeValueAsString(userNotificationDtoList))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
	}

	@Test
	@WithMockUser(username = "user", roles = {"USER"})
	void createUserNotificationsThrowException() throws Exception {
		// mock behaviour of notification service - throws an exception
		when(userNotificationService.createUserNotification(any())).thenThrow(new EntityNotFoundException());

		// mock HTTP request to create user notification
		mockMvc.perform(MockMvcRequestBuilders.post("/notify/notification/create-notifications")
				.content(new ObjectMapper().writeValueAsString(userNotificationDtoList))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}

	@Test
	@WithMockUser(username = "user", roles = {"USER"})
	void toggleNotificationStatus() throws Exception {
		// mock data
		UserNotificationDto userNotificationResponse = UserNotificationDto.builder()
				.id(7L)
				.message("mock message")
				.description("mock description")
				.status(NotificationStatus.UNSEEN)
				.severity(NotificationSeverity.ERROR)
				.createdOn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-01-01 10:10:10"))
				.userId(1L)
				.build();

		when(userNotificationService.toggleNotificationStatus(anyLong())).thenReturn(userNotificationResponse);

		// mock HTTP request to create user notification
		mockMvc.perform(MockMvcRequestBuilders.put("/notify/notification/toggle-status/7")
				.content(new ObjectMapper().writeValueAsString(userNotificationResponse))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("UNSEEN"));
	}

	@Test
	@WithMockUser(username = "user", roles = {"USER"})
	void toggleNotificationStatusThrowException() throws Exception {
		when(userNotificationService.toggleNotificationStatus(anyLong()))
				.thenThrow(new EntityNotFoundException());

		// mock HTTP request to create user notification
		mockMvc.perform(MockMvcRequestBuilders.put("/notify/notification/toggle-status/7")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
	}
}
