package com.admin.service.service.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.admin.service.client.pharmacy.ManagerClient;
import com.admin.service.dto.ManagerDto;
import com.admin.service.exception.ResourceException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

@ContextConfiguration(classes = { AdminManagerServiceImpl.class })
@ExtendWith(SpringExtension.class)
class AdminManagerServiceImplTest {
    @Autowired
    private AdminManagerServiceImpl adminManagerServiceImpl;

    @MockBean
    private WebClient.Builder builder;

    @MockBean
    private ManagerClient managerClient;

    @Test
    void testGetAllManagers() {
        when(managerClient.getManagers((Integer) any(), (Integer) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        assertNull(adminManagerServiceImpl.getAllManagers(10, 3));
        verify(managerClient).getManagers((Integer) any(), (Integer) any());
    }

    @Test
    void testGetAllManagers3() {
        when(managerClient.getManagers((Integer) any(), (Integer) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value",
                        ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> adminManagerServiceImpl.getAllManagers(10, 3));
        verify(managerClient).getManagers((Integer) any(), (Integer) any());
    }

    @Test
    void testGetManagerById() {
        when(managerClient.getManagerById((Long) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        assertNull(adminManagerServiceImpl.getManagerById(123L));
        verify(managerClient).getManagerById((Long) any());
    }

    @Test
    void testGetManagerById2() {
        when(managerClient.getManagerById((Long) any())).thenReturn(null);
        assertThrows(ResourceException.class, () -> adminManagerServiceImpl.getManagerById(123L));
        verify(managerClient).getManagerById((Long) any());
    }

    @Test
    void testGetManagerById3() {
        ResponseEntity<ManagerDto> responseEntity = (ResponseEntity<ManagerDto>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value",
                        ResourceException.ErrorType.CREATED));
        when(managerClient.getManagerById((Long) any())).thenReturn(responseEntity);
        assertThrows(ResourceException.class, () -> adminManagerServiceImpl.getManagerById(123L));
        verify(managerClient).getManagerById((Long) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testGetAllPendingRequests() {
        when(managerClient.getAllPendingRequests((Integer) any(), (Integer) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        assertNull(adminManagerServiceImpl.getAllPendingRequests(10, 3));
        verify(managerClient).getAllPendingRequests((Integer) any(), (Integer) any());
    }

    @Test
    void testGetAllPendingRequests3() {
        when(managerClient.getAllPendingRequests((Integer) any(), (Integer) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value",
                        ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> adminManagerServiceImpl.getAllPendingRequests(10, 3));
        verify(managerClient).getAllPendingRequests((Integer) any(), (Integer) any());
    }

    @Test
    void testApprovePendingRequestById() {
        when(managerClient.approvePendingRequestById((Long) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        assertNull(adminManagerServiceImpl.approvePendingRequestById(123L));
        verify(managerClient).approvePendingRequestById((Long) any());
    }

    @Test
    void testApprovePendingRequestById2() {
        when(managerClient.approvePendingRequestById((Long) any())).thenReturn(null);
        assertThrows(ResourceException.class, () -> adminManagerServiceImpl.approvePendingRequestById(123L));
        verify(managerClient).approvePendingRequestById((Long) any());
    }

    @Test
    void testApprovePendingRequestById3() {
        ResponseEntity<String> responseEntity = (ResponseEntity<String>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value",
                        ResourceException.ErrorType.CREATED));
        when(managerClient.approvePendingRequestById((Long) any())).thenReturn(responseEntity);
        assertThrows(ResourceException.class, () -> adminManagerServiceImpl.approvePendingRequestById(123L));
        verify(managerClient).approvePendingRequestById((Long) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testRejectPendingRequestById() {
        when(managerClient.rejectPendingRequestById((Long) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        assertNull(adminManagerServiceImpl.rejectPendingRequestById(123L));
        verify(managerClient).rejectPendingRequestById((Long) any());
    }

    @Test
    void testRejectPendingRequestById2() {
        when(managerClient.rejectPendingRequestById((Long) any())).thenReturn(null);
        assertThrows(ResourceException.class, () -> adminManagerServiceImpl.rejectPendingRequestById(123L));
        verify(managerClient).rejectPendingRequestById((Long) any());
    }

    @Test
    void testRejectPendingRequestById3() {
        ResponseEntity<String> responseEntity = (ResponseEntity<String>) mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value",
                        ResourceException.ErrorType.CREATED));
        when(managerClient.rejectPendingRequestById((Long) any())).thenReturn(responseEntity);
        assertThrows(ResourceException.class, () -> adminManagerServiceImpl.rejectPendingRequestById(123L));
        verify(managerClient).rejectPendingRequestById((Long) any());
        verify(responseEntity).getBody();
    }

    @Test
    void testGetManagersWithFilter() {
        when(managerClient.getManagersByFiltering((String) any(), (Integer) any(), (Integer) any(), (String) any(),
                (String) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        assertNull(adminManagerServiceImpl.getManagersWithFilter("Status", 10, 3, "Sort By", "name"));
        verify(managerClient).getManagersByFiltering((String) any(), (Integer) any(), (Integer) any(), (String) any(),
                (String) any());
    }
}
