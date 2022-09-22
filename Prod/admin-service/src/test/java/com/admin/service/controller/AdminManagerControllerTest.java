
package com.admin.service.controller;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.admin.service.client.auth.RoleClient;
import com.admin.service.client.notify.EmailClient;
import com.admin.service.entity.ApprovalStatus;
import com.admin.service.dto.PageableResponse;
import com.admin.service.dto.ManagerDto;
import com.admin.service.service.AdminManagerService;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AdminManagerControllerTest {

	@InjectMocks
	AdminManagerController adminManagerController;

	@Mock
	AdminManagerService adminManagerService;

	@Mock
	RoleClient roleClient;

	@Mock
	EmailClient emailClient;

	private static WireMockServer wireMockServer;

	@Test
	void approvePendingRequestById(){
		/*wireMockServer = new WireMockServer(new WireMockConfiguration().port(8085));
		wireMockServer.start();
		WireMock.configureFor("localhost", 8085);

		stubFor(WireMock.post(WireMock.urlMatching("/notify/mail/send")).willReturn(aResponse()
						.withStatus(200)));*/
		when(adminManagerService.approvePendingRequestById(anyLong())).thenReturn("Sample Response for approval test");
		when(roleClient.approveManager( anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		doNothing().when(emailClient).sendEmail(any());
		ResponseEntity<?> responseEntity = adminManagerController.approvePendingRequestById(1L);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);



	}

	@Test
	void rejectPendingRequestById(){
		/*wireMockServer = new WireMockServer(new WireMockConfiguration().port(8085));
		wireMockServer.start();
		WireMock.configureFor("localhost", 8085);

		stubFor(WireMock.post(WireMock.urlMatching("/notify/mail/send")).willReturn(aResponse()
						.withStatus(200)));*/
		when(adminManagerService.rejectPendingRequestById(anyLong())).thenReturn("Sample Response for approval test");
		when(roleClient.approveManager( anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		doNothing().when(emailClient).sendEmail(any());
		ResponseEntity<?> responseEntity = adminManagerController.rejectPendingRequestById(1L);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);



	}

	@Test
	void getAllPendingRequests() throws Exception {

		List<ManagerDto> pendingRequest = new ArrayList<>();
		ManagerDto manager = new ManagerDto();
		PageableResponse<ManagerDto> managerResponse = new PageableResponse<ManagerDto>();
		manager.setManagerId(1L);
		manager.setLicenseNo("123");
		manager.setPhoneNo("1234567890");
		manager.setApprovalStatus(ApprovalStatus.PENDING);
		pendingRequest.add(manager);
		managerResponse.setData(pendingRequest);
		when(adminManagerService.getAllPendingRequests(0, 1)).thenReturn(managerResponse);
		ResponseEntity<?> responseEntity = adminManagerController.getPendingRequests(0, 1);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		assertEquals(managerResponse, responseEntity.getBody());

	}

	@Test
	void getAllManagers() throws Exception {
		List<ManagerDto> pendingRequest = new ArrayList<>();
		ManagerDto manager = new ManagerDto();
		PageableResponse<ManagerDto> managerResponse = new PageableResponse<ManagerDto>();
		manager.setManagerId(1L);
		manager.setLicenseNo("123");
		manager.setPhoneNo("1234567890");
		manager.setApprovalStatus(ApprovalStatus.PENDING);
		pendingRequest.add(manager);
		managerResponse.setData(pendingRequest);
		when(adminManagerService.getAllManagers(0, 1)).thenReturn(managerResponse);
		ResponseEntity<?> responseEntity = adminManagerController.getAllManagers(0, 1);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		assertEquals(managerResponse, responseEntity.getBody());

	}

	@Test
	void getManagerById() throws Exception {

		ManagerDto manager = new ManagerDto();
		manager.setManagerId(1L);
		manager.setLicenseNo("123");
		manager.setPhoneNo("1234567890");
		manager.setApprovalStatus(ApprovalStatus.PENDING);

		when(adminManagerService.getManagerById(1L)).thenReturn(manager);
		ResponseEntity<?> responseEntity = adminManagerController.getManagerById(1L);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		assertEquals(manager, responseEntity.getBody());

	}

	@Test
	void getManagersByFiltering() throws Exception {
		List<ManagerDto> pendingRequest=new ArrayList<>();
		ManagerDto manager=new ManagerDto();
		PageableResponse<ManagerDto> managerResponse=new PageableResponse<ManagerDto>();
		manager.setManagerId(1L);
		manager.setLicenseNo("123");
		manager.setPhoneNo("1234567890");
		manager.setApprovalStatus(ApprovalStatus.PENDING);
		pendingRequest.add(manager);
		managerResponse.setData(pendingRequest);
		when(adminManagerService.getManagersWithFilter("PENDING",0,1,"managerId","sam")).thenReturn(managerResponse);
		ResponseEntity<?> responseEntity=adminManagerController.getManagersByFiltering("PENDING",0,1,"managerId","sam");
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
		assertEquals(managerResponse, responseEntity.getBody());

	}
	

}
