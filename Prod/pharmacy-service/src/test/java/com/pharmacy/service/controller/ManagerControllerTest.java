package com.pharmacy.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharmacy.service.dto.ManagerDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.model.ApprovalStatus;
import com.pharmacy.service.service.ManagerService;
import com.pharmacy.service.utils.JwtUtils;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ManagerController.class})
@ExtendWith(SpringExtension.class)
class ManagerControllerTest {
    @Autowired
    private ManagerController managerController;

    @MockBean
    private ManagerService managerService;



//    @MockBean
//    private JwtUtils JwtUtils;

    /**
     * Method under test: {@link ManagerController#getManagers(Integer, Integer)}
     */

    @BeforeAll
    static void setup(){
        mockStatic(JwtUtils.class);
    }
    @Test
    void testGetManagers() throws Exception {
        when(managerService.getManagers(any(), any())).thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/manager/get-manager");
        MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"data\":null,\"pageNumber\":null,\"pageSize\":null,\"totalRecords\":null,\"totalPages\":null,\"isLastPage"
                                        + "\":null}"));
    }

    @Test
    void testGetTotalNoOfApprovedManagers() throws Exception {
        when(managerService.noOfManagers(ApprovalStatus.APPROVED)).thenReturn(5L);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/manager/total-managers/approved");
        MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "5"));
    }

    /**
     * Method under test: {@link ManagerController#getManagers(Integer, Integer)}
     */
    @Test
    void testGetManagers2() throws Exception {
        when(managerService.getManagers(any(), any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/pharmacy/manager/get-manager");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link ManagerController#getAllPendingRequests(Integer, Integer)}
     */
    @Test
    void testGetAllPendingRequests() throws Exception {
        when(managerService.getAllPendingRequests(any(), any())).thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/manager/get-manager/pending");
        MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"data\":null,\"pageNumber\":null,\"pageSize\":null,\"totalRecords\":null,\"totalPages\":null,\"isLastPage"
                                        + "\":null}"));
    }

    /**
     * Method under test: {@link ManagerController#getAllPendingRequests(Integer, Integer)}
     */
    @Test
    void testGetAllPendingRequests2() throws Exception {
        when(managerService.getAllPendingRequests(any(), any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/manager/get-manager/pending");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link ManagerController#getAllApprovedManagers(Integer, Integer)}
     */
    @Test
    void testGetAllApprovedManagers() throws Exception {
        when(managerService.getAllApprovedManagers(any(), any()))
                .thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/manager/get-manager/approved");
        MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"data\":null,\"pageNumber\":null,\"pageSize\":null,\"totalRecords\":null,\"totalPages\":null,\"isLastPage"
                                        + "\":null}"));
    }

    /**
     * Method under test: {@link ManagerController#getAllApprovedManagers(Integer, Integer)}
     */
    @Test
    void testGetAllApprovedManagers2() throws Exception {
        when(managerService.getAllApprovedManagers(any(), any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/manager/get-manager/approved");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link ManagerController#rejectPendingRequestById(Long)}
     */
    @Test
    void testRejectPendingRequestById() throws Exception {
        when(managerService.rejectPendingRequestById(any())).thenReturn("42");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/pharmacy/manager/rejection/{id}",
                123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("42"));
    }

    /**
     * Method under test: {@link ManagerController#rejectPendingRequestById(Long)}
     */
    @Test
    void testRejectPendingRequestById2() throws Exception {
        when(managerService.rejectPendingRequestById(any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/pharmacy/manager/rejection/{id}",
                123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link ManagerController#approvePendingRequestById(Long)}
     */
    @Test
    void testApprovePendingRequestById() throws Exception {
        when(managerService.approvePendingRequestById(any())).thenReturn("42");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/pharmacy/manager/approval/{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("42"));
    }

    /**
     * Method under test: {@link ManagerController#approvePendingRequestById(Long)}
     */
    @Test
    void testApprovePendingRequestById2() throws Exception {
        when(managerService.approvePendingRequestById(any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/pharmacy/manager/approval/{id}",
                123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link ManagerController#deleteManager(String, Long)}
     */
    @Test
    void testDeleteManager() throws Exception {

        when(JwtUtils.verifyId(any(), any(), anyBoolean())).thenReturn(true);
        doNothing().when(managerService).deleteManager(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/pharmacy/manager/delete-manager/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteManager1() throws Exception {

        when(JwtUtils.verifyId(any(), any(), anyBoolean())).thenReturn(false);
        doThrow(new ResponseStatusException(HttpStatus.CONTINUE)).when(managerService).deleteManager(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/pharmacy/manager/delete-manager/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    /**
     * Method under test: {@link ManagerController#getManagerById(String, Long)}
     */
    @Test
    void testGetManagerById() throws Exception {

        when(JwtUtils.verifyId(any(), any(), anyBoolean())).thenReturn(true);
        when(managerService.getManagerById(any())).thenReturn(new ManagerDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/manager/get-manager/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(200));
    }

    /**
     * Method under test: {@link ManagerController#getManagersByFiltering(String, Integer, Integer, String, String)}
     */
    @Test
    void testGetManagersByFiltering() throws Exception {
        when(managerService.getManagersWithFilter(any(), any(), any(), any(),
                any())).thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/manager/get-manager-with-filter")
                .param("sortBy", "foo");
        MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"data\":null,\"pageNumber\":null,\"pageSize\":null,\"totalRecords\":null,\"totalPages\":null,\"isLastPage"
                                        + "\":null}"));
    }

    /**
     * Method under test: {@link ManagerController#getManagersByFiltering(String, Integer, Integer, String, String)}
     */
    @Test
    void testGetManagersByFiltering2() throws Exception {
        when(managerService.getManagersWithFilter(any(), any(), any(), any(),
                any())).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/manager/get-manager-with-filter")
                .param("sortBy", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link ManagerController#saveManager(Long, ManagerDto)}
     */
    @Test
    void testSaveManager() throws Exception {
        when(managerService.createManager(any()))
                .thenReturn(new ManagerDto());

        ManagerDto managerDto = new ManagerDto();
        managerDto.setApprovalStatus(ApprovalStatus.APPROVED);
        managerDto.setLicenseNo("License No");
        managerDto.setManagerId(123L);
        managerDto.setName("Name");
        managerDto.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        managerDto.setRegistrationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        String content = (new ObjectMapper()).writeValueAsString(managerDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/pharmacy/manager/create-manager/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(201));
    }

    /**
     * Method under test: {@link ManagerController#updateManager(String, Long, ManagerDto)}
     */
    @Test
    void testUpdateManager() throws Exception {
//
        when(JwtUtils.verifyId(any(), any(), anyBoolean())).thenReturn(false);
        when(managerService.updateManager(any())).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));

        ManagerDto managerDto = new ManagerDto();
        managerDto.setApprovalStatus(ApprovalStatus.APPROVED);
        managerDto.setLicenseNo("License No");
        managerDto.setManagerId(123L);
        managerDto.setName("Name");
        managerDto.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        managerDto.setRegistrationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        String content = (new ObjectMapper()).writeValueAsString(managerDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/pharmacy/manager/update-manager/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }

    @Test
    void getManager2() throws Exception {

        when(JwtUtils.verifyId(any(), any(), anyBoolean())).thenReturn(false);
        when(managerService.getManagerById(any())).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/manager/get-manager/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(401));
    }

    @Test
    void getManager3() throws Exception {

        when(JwtUtils.verifyId(any(), any(), anyBoolean())).thenReturn(false);
        when(managerService.getManagerById(any())).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pharmacy/manager/get-manager/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(401));
    }


    @Test
    void testUpdateManager1() throws Exception {

        when(managerService.updateManager(any())).thenReturn(new ManagerDto());

        ManagerDto managerDto = new ManagerDto();
        managerDto.setApprovalStatus(ApprovalStatus.APPROVED);
        managerDto.setLicenseNo("License No");
        managerDto.setManagerId(123L);
        managerDto.setName("Name");
        managerDto.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        managerDto.setRegistrationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        String content = (new ObjectMapper()).writeValueAsString(managerDto);

        when(JwtUtils.verifyId(any(), any(), anyBoolean())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/pharmacy/manager/update-manager/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(managerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    void testUpdateManager2() throws Exception {
        when(managerService.updateManager(any())).thenReturn(new ManagerDto());

        ManagerDto managerDto = new ManagerDto();
        managerDto.setApprovalStatus(ApprovalStatus.APPROVED);
        managerDto.setLicenseNo("License No");
        managerDto.setManagerId(123L);
        managerDto.setName("Name");
        managerDto.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        managerDto.setRegistrationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        String content = (new ObjectMapper()).writeValueAsString(managerDto);

        when(JwtUtils.verifyId(any(), any(), anyBoolean())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/pharmacy/manager/update-manager/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        assertThrows(ResponseStatusException.class, () -> managerController.updateManager("Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==", 123L, managerDto));

    }
}

