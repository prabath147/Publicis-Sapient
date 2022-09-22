package com.pharmacy.service.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pharmacy.service.dto.ManagerDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.exception.ResourceException;
import com.pharmacy.service.model.ApprovalStatus;
import com.pharmacy.service.model.Manager;
import com.pharmacy.service.repository.ManagerRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ManagerServiceImplementation.class})
@ExtendWith(SpringExtension.class)
class ManagerServiceImplementationTest {
    @MockBean
    private ManagerRepository managerRepository;

    @Autowired
    private ManagerServiceImplementation managerServiceImplementation;

    @MockBean
    private ModelMapper modelMapper;

    /**
     * Method under test: {@link ManagerServiceImplementation#createManager(ManagerDto)}
     */
    @Test
    void testCreateManager() {
        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        when(managerRepository.save((Manager) any())).thenReturn(manager);

        Manager manager1 = new Manager();
        manager1.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setLastModified(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        manager1.setLicenseNo("License No");
        manager1.setManagerId(123L);
        manager1.setName("Name");
        manager1.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setRegistrationDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        when(modelMapper.map((Object) any(), (Class<Manager>) any())).thenReturn(manager1);
        assertThrows(ResourceException.class, () -> managerServiceImplementation.createManager(new ManagerDto()));
        verify(managerRepository).save((Manager) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#createManager(ManagerDto)}
     */
    @Test
    void testCreateManager2() {
        when(managerRepository.save((Manager) any()))
                .thenThrow(new ResourceException("Manager", "Manager", "Field Value", ResourceException.ErrorType.CREATED));

        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        when(modelMapper.map((Object) any(), (Class<Manager>) any())).thenReturn(manager);
        assertThrows(ResourceException.class, () -> managerServiceImplementation.createManager(new ManagerDto()));
        verify(managerRepository).save((Manager) any());
        verify(modelMapper).map((Object) any(), (Class<Manager>) any());
    }

    @Test
    void testNoOfManagers() {
        when(managerRepository.countAllByApprovalStatus((ApprovalStatus) any())).thenReturn(3L);
        assertEquals(3L, managerServiceImplementation.noOfManagers(ApprovalStatus.APPROVED).longValue());
        verify(managerRepository).countAllByApprovalStatus((ApprovalStatus) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#noOfManagers(ApprovalStatus)}
     */
    @Test
    void testNoOfManagers2() {
        when(managerRepository.countAllByApprovalStatus((ApprovalStatus) any())).thenReturn(3L);
        assertEquals(3L, managerServiceImplementation.noOfManagers(ApprovalStatus.PENDING).longValue());
        verify(managerRepository).countAllByApprovalStatus((ApprovalStatus) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#noOfManagers(ApprovalStatus)}
     */
    @Test
    void testNoOfManagers3() {
        when(managerRepository.countAllByApprovalStatus((ApprovalStatus) any())).thenReturn(3L);
        assertEquals(3L, managerServiceImplementation.noOfManagers(ApprovalStatus.REJECTED).longValue());
        verify(managerRepository).countAllByApprovalStatus((ApprovalStatus) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#noOfManagers(ApprovalStatus)}
     */
    @Test
    void testNoOfManagers4() {
        when(managerRepository.countAllByApprovalStatus((ApprovalStatus) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> managerServiceImplementation.noOfManagers(ApprovalStatus.APPROVED));
        verify(managerRepository).countAllByApprovalStatus((ApprovalStatus) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists() {
        when(managerRepository.existsById((Long) any())).thenReturn(true);
        assertTrue(managerServiceImplementation.checkIfExists(123L));
        verify(managerRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists2() {
        when(managerRepository.existsById((Long) any())).thenReturn(false);
        assertFalse(managerServiceImplementation.checkIfExists(123L));
        verify(managerRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists3() {
        when(managerRepository.existsById((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> managerServiceImplementation.checkIfExists(123L));
        verify(managerRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#getManagers(Integer, Integer)}
     */
    @Test
    void testGetManagers() {
        ArrayList<Manager> managerList = new ArrayList<>();
        when(managerRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(managerList));
        PageableResponse<ManagerDto> actualManagers = managerServiceImplementation.getManagers(10, 3);
        assertEquals(managerList, actualManagers.getData());
        assertEquals(0L, actualManagers.getTotalRecords().longValue());
        assertEquals(1, actualManagers.getTotalPages().intValue());
        assertEquals(0, actualManagers.getPageSize().intValue());
        assertEquals(0, actualManagers.getPageNumber().intValue());
        assertTrue(actualManagers.getIsLastPage());
        verify(managerRepository).findAll((Pageable) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#getManagers(Integer, Integer)}
     */
    @Test
    void testGetManagers2() {
        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("lastModified");
        manager.setManagerId(123L);
        manager.setName("lastModified");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<Manager> managerList = new ArrayList<>();
        managerList.add(manager);
        PageImpl<Manager> pageImpl = new PageImpl<>(managerList);
        when(managerRepository.findAll((Pageable) any())).thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ManagerDto>) any())).thenReturn(new ManagerDto());
        PageableResponse<ManagerDto> actualManagers = managerServiceImplementation.getManagers(10, 3);
        assertEquals(1, actualManagers.getData().size());
        assertEquals(1L, actualManagers.getTotalRecords().longValue());
        assertEquals(1, actualManagers.getTotalPages().intValue());
        assertEquals(1, actualManagers.getPageSize().intValue());
        assertEquals(0, actualManagers.getPageNumber().intValue());
        assertTrue(actualManagers.getIsLastPage());
        verify(managerRepository).findAll((Pageable) any());
        verify(modelMapper).map((Object) any(), (Class<ManagerDto>) any());
    }

    @Test
    void testGetManagerById() {
        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Manager> ofResult = Optional.of(manager);
        when(managerRepository.findById((Long) any())).thenReturn(ofResult);
        ManagerDto managerDto = new ManagerDto();
        when(modelMapper.map((Object) any(), (Class<ManagerDto>) any())).thenReturn(managerDto);
        assertSame(managerDto, managerServiceImplementation.getManagerById(123L));
        verify(managerRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<ManagerDto>) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#getManagerById(Long)}
     */
    @Test
    void testGetManagerById2() {
        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Manager> ofResult = Optional.of(manager);
        when(managerRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<ManagerDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> managerServiceImplementation.getManagerById(123L));
        verify(managerRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<ManagerDto>) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#getManagerById(Long)}
     */
    @Test
    void testGetManagerById3() {
        when(managerRepository.findById((Long) any())).thenReturn(Optional.empty());
//        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");
//        when(modelMapper.map((Object) any(), (Class<ManagerDto>) any())).thenReturn(new ManagerDto());
        assertThrows(ResourceException.class, () -> managerServiceImplementation.getManagerById(123L));
        verify(managerRepository).findById((Long) any());
//        verify(modelMapper).map((Object) any(), (Class<Object>) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#getAllPendingRequests(Integer, Integer)}
     */
    @Test
    void testGetAllPendingRequests() {
        ArrayList<Manager> managerList = new ArrayList<>();
        when(managerRepository.findAllByApprovalStatus((ApprovalStatus) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(managerList));
        PageableResponse<ManagerDto> actualAllPendingRequests = managerServiceImplementation.getAllPendingRequests(10, 3);
        assertEquals(managerList, actualAllPendingRequests.getData());
        assertEquals(0L, actualAllPendingRequests.getTotalRecords().longValue());
        assertEquals(1, actualAllPendingRequests.getTotalPages().intValue());
        assertEquals(0, actualAllPendingRequests.getPageSize().intValue());
        assertEquals(0, actualAllPendingRequests.getPageNumber().intValue());
        assertTrue(actualAllPendingRequests.getIsLastPage());
        verify(managerRepository).findAllByApprovalStatus((ApprovalStatus) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#getAllPendingRequests(Integer, Integer)}
     */
    @Test
    void testGetAllPendingRequests2() {
        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<Manager> managerList = new ArrayList<>();
        managerList.add(manager);
        PageImpl<Manager> pageImpl = new PageImpl<>(managerList);
        when(managerRepository.findAllByApprovalStatus((ApprovalStatus) any(), (Pageable) any())).thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ManagerDto>) any())).thenReturn(new ManagerDto());
        PageableResponse<ManagerDto> actualAllPendingRequests = managerServiceImplementation.getAllPendingRequests(10, 3);
        assertEquals(1, actualAllPendingRequests.getData().size());
        assertEquals(1L, actualAllPendingRequests.getTotalRecords().longValue());
        assertEquals(1, actualAllPendingRequests.getTotalPages().intValue());
        assertEquals(1, actualAllPendingRequests.getPageSize().intValue());
        assertEquals(0, actualAllPendingRequests.getPageNumber().intValue());
        assertTrue(actualAllPendingRequests.getIsLastPage());
        verify(managerRepository).findAllByApprovalStatus((ApprovalStatus) any(), (Pageable) any());
        verify(modelMapper).map((Object) any(), (Class<ManagerDto>) any());
    }

    @Test
    void testGetAllApprovedManagers() {
        ArrayList<Manager> managerList = new ArrayList<>();
        when(managerRepository.findAllByApprovalStatus((ApprovalStatus) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(managerList));
        PageableResponse<ManagerDto> actualAllApprovedManagers = managerServiceImplementation.getAllApprovedManagers(10,
                3);
        assertEquals(managerList, actualAllApprovedManagers.getData());
        assertEquals(0L, actualAllApprovedManagers.getTotalRecords().longValue());
        assertEquals(1, actualAllApprovedManagers.getTotalPages().intValue());
        assertEquals(0, actualAllApprovedManagers.getPageSize().intValue());
        assertEquals(0, actualAllApprovedManagers.getPageNumber().intValue());
        assertTrue(actualAllApprovedManagers.getIsLastPage());
        verify(managerRepository).findAllByApprovalStatus((ApprovalStatus) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#getAllApprovedManagers(Integer, Integer)}
     */
    @Test
    void testGetAllApprovedManagers2() {
        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<Manager> managerList = new ArrayList<>();
        managerList.add(manager);
        PageImpl<Manager> pageImpl = new PageImpl<>(managerList);
        when(managerRepository.findAllByApprovalStatus((ApprovalStatus) any(), (Pageable) any())).thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ManagerDto>) any())).thenReturn(new ManagerDto());
        PageableResponse<ManagerDto> actualAllApprovedManagers = managerServiceImplementation.getAllApprovedManagers(10,
                3);
        assertEquals(1, actualAllApprovedManagers.getData().size());
        assertEquals(1L, actualAllApprovedManagers.getTotalRecords().longValue());
        assertEquals(1, actualAllApprovedManagers.getTotalPages().intValue());
        assertEquals(1, actualAllApprovedManagers.getPageSize().intValue());
        assertEquals(0, actualAllApprovedManagers.getPageNumber().intValue());
        assertTrue(actualAllApprovedManagers.getIsLastPage());
        verify(managerRepository).findAllByApprovalStatus((ApprovalStatus) any(), (Pageable) any());
        verify(modelMapper).map((Object) any(), (Class<ManagerDto>) any());
    }

    @Test
    void testRejectPendingRequestById() {
        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Manager> ofResult = Optional.of(manager);
        when(managerRepository.findById((Long) any())).thenReturn(ofResult);
        assertEquals("Manager with ID - 123 is REJECTED", managerServiceImplementation.rejectPendingRequestById(123L));
        verify(managerRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#rejectPendingRequestById(Long)}
     */
    @Test
    void testRejectPendingRequestById2() {
        Manager manager = mock(Manager.class);
        when(manager.getApprovalStatus()).thenReturn(ApprovalStatus.PENDING);
        doNothing().when(manager).setApprovalStatus((ApprovalStatus) any());
        doNothing().when(manager).setLastModified((Date) any());
        doNothing().when(manager).setLicenseNo((String) any());
        doNothing().when(manager).setManagerId((Long) any());
        doNothing().when(manager).setName((String) any());
        doNothing().when(manager).setPhoneNo((String) any());
        doNothing().when(manager).setRegistrationDate((Date) any());
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Manager> ofResult = Optional.of(manager);
        doNothing().when(managerRepository).deleteById((Long) any());
        when(managerRepository.existsById((Long) any())).thenReturn(true);
        when(managerRepository.findById((Long) any())).thenReturn(ofResult);
        assertEquals("Manager with ID - 123 is REJECTED", managerServiceImplementation.rejectPendingRequestById(123L));
        verify(managerRepository).existsById((Long) any());
        verify(managerRepository).findById((Long) any());
        verify(managerRepository).deleteById((Long) any());
        verify(manager).getApprovalStatus();
        verify(manager, atLeast(1)).setApprovalStatus((ApprovalStatus) any());
        verify(manager, atLeast(1)).setLastModified((Date) any());
        verify(manager).setLicenseNo((String) any());
        verify(manager).setManagerId((Long) any());
        verify(manager).setName((String) any());
        verify(manager).setPhoneNo((String) any());
        verify(manager).setRegistrationDate((Date) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#rejectPendingRequestById(Long)}
     */
    @Test
    void testRejectPendingRequestById3() {
        Manager manager = mock(Manager.class);
        when(manager.getApprovalStatus()).thenReturn(ApprovalStatus.PENDING);
        doNothing().when(manager).setApprovalStatus((ApprovalStatus) any());
        doNothing().when(manager).setLastModified((Date) any());
        doNothing().when(manager).setLicenseNo((String) any());
        doNothing().when(manager).setManagerId((Long) any());
        doNothing().when(manager).setName((String) any());
        doNothing().when(manager).setPhoneNo((String) any());
        doNothing().when(manager).setRegistrationDate((Date) any());
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Manager> ofResult = Optional.of(manager);
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(managerRepository)
                .deleteById((Long) any());
        when(managerRepository.existsById((Long) any())).thenReturn(true);
        when(managerRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> managerServiceImplementation.rejectPendingRequestById(123L));
        verify(managerRepository).existsById((Long) any());
        verify(managerRepository).findById((Long) any());
        verify(managerRepository).deleteById((Long) any());
        verify(manager).getApprovalStatus();
        verify(manager, atLeast(1)).setApprovalStatus((ApprovalStatus) any());
        verify(manager, atLeast(1)).setLastModified((Date) any());
        verify(manager).setLicenseNo((String) any());
        verify(manager).setManagerId((Long) any());
        verify(manager).setName((String) any());
        verify(manager).setPhoneNo((String) any());
        verify(manager).setRegistrationDate((Date) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#rejectPendingRequestById(Long)}
     */
    @Test
    void testRejectPendingRequestById4() {
        Manager manager = mock(Manager.class);
        when(manager.getApprovalStatus()).thenReturn(ApprovalStatus.PENDING);
        doNothing().when(manager).setApprovalStatus((ApprovalStatus) any());
        doNothing().when(manager).setLastModified((Date) any());
        doNothing().when(manager).setLicenseNo((String) any());
        doNothing().when(manager).setManagerId((Long) any());
        doNothing().when(manager).setName((String) any());
        doNothing().when(manager).setPhoneNo((String) any());
        doNothing().when(manager).setRegistrationDate((Date) any());
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Manager> ofResult = Optional.of(manager);
        doNothing().when(managerRepository).deleteById((Long) any());
        when(managerRepository.existsById((Long) any())).thenReturn(false);
        when(managerRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> managerServiceImplementation.rejectPendingRequestById(123L));
        verify(managerRepository).existsById((Long) any());
        verify(managerRepository).findById((Long) any());
        verify(manager).getApprovalStatus();
        verify(manager, atLeast(1)).setApprovalStatus((ApprovalStatus) any());
        verify(manager, atLeast(1)).setLastModified((Date) any());
        verify(manager).setLicenseNo((String) any());
        verify(manager).setManagerId((Long) any());
        verify(manager).setName((String) any());
        verify(manager).setPhoneNo((String) any());
        verify(manager).setRegistrationDate((Date) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#rejectPendingRequestById(Long)}
     */
    @Test
    void testRejectPendingRequestById5() {
        doNothing().when(managerRepository).deleteById((Long) any());
        when(managerRepository.existsById((Long) any())).thenReturn(true);
        when(managerRepository.findById((Long) any())).thenReturn(Optional.empty());
        Manager manager = mock(Manager.class);
        when(manager.getApprovalStatus()).thenReturn(ApprovalStatus.PENDING);
        doNothing().when(manager).setApprovalStatus((ApprovalStatus) any());
        doNothing().when(manager).setLastModified((Date) any());
        doNothing().when(manager).setLicenseNo((String) any());
        doNothing().when(manager).setManagerId((Long) any());
        doNothing().when(manager).setName((String) any());
        doNothing().when(manager).setPhoneNo((String) any());
        doNothing().when(manager).setRegistrationDate((Date) any());
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        assertThrows(ResourceException.class, () -> managerServiceImplementation.rejectPendingRequestById(123L));
        verify(managerRepository).findById((Long) any());
        verify(manager).setApprovalStatus((ApprovalStatus) any());
        verify(manager).setLastModified((Date) any());
        verify(manager).setLicenseNo((String) any());
        verify(manager).setManagerId((Long) any());
        verify(manager).setName((String) any());
        verify(manager).setPhoneNo((String) any());
        verify(manager).setRegistrationDate((Date) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#approvePendingRequestById(Long)}
     */
    @Test
    void testApprovePendingRequestById() {
        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Manager> ofResult = Optional.of(manager);
        when(managerRepository.findById((Long) any())).thenReturn(ofResult);
        assertEquals("Manager with ID -123 is already APPROVED",
                managerServiceImplementation.approvePendingRequestById(123L));
        verify(managerRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#approvePendingRequestById(Long)}
     */
    @Test
    void testApprovePendingRequestById2() {
        Manager manager = mock(Manager.class);
        when(manager.getApprovalStatus()).thenReturn(ApprovalStatus.PENDING);
        doNothing().when(manager).setApprovalStatus((ApprovalStatus) any());
        doNothing().when(manager).setLastModified((Date) any());
        doNothing().when(manager).setLicenseNo((String) any());
        doNothing().when(manager).setManagerId((Long) any());
        doNothing().when(manager).setName((String) any());
        doNothing().when(manager).setPhoneNo((String) any());
        doNothing().when(manager).setRegistrationDate((Date) any());
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Manager> ofResult = Optional.of(manager);

        Manager manager1 = new Manager();
        manager1.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setLastModified(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        manager1.setLicenseNo("License No");
        manager1.setManagerId(123L);
        manager1.setName("Name");
        manager1.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setRegistrationDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        when(managerRepository.save((Manager) any())).thenReturn(manager1);
        when(managerRepository.findById((Long) any())).thenReturn(ofResult);
        assertEquals("Manager with ID - 123 is PENDING", managerServiceImplementation.approvePendingRequestById(123L));
        verify(managerRepository).save((Manager) any());
        verify(managerRepository).findById((Long) any());
        verify(manager, atLeast(1)).getApprovalStatus();
        verify(manager, atLeast(1)).setApprovalStatus((ApprovalStatus) any());
        verify(manager, atLeast(1)).setLastModified((Date) any());
        verify(manager).setLicenseNo((String) any());
        verify(manager).setManagerId((Long) any());
        verify(manager).setName((String) any());
        verify(manager).setPhoneNo((String) any());
        verify(manager).setRegistrationDate((Date) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#approvePendingRequestById(Long)}
     */
    @Test
    void testApprovePendingRequestById3() {
        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        when(managerRepository.save((Manager) any())).thenReturn(manager);
        when(managerRepository.findById((Long) any())).thenReturn(Optional.empty());
        Manager manager1 = mock(Manager.class);
        when(manager1.getApprovalStatus()).thenReturn(ApprovalStatus.PENDING);
        doNothing().when(manager1).setApprovalStatus((ApprovalStatus) any());
        doNothing().when(manager1).setLastModified((Date) any());
        doNothing().when(manager1).setLicenseNo((String) any());
        doNothing().when(manager1).setManagerId((Long) any());
        doNothing().when(manager1).setName((String) any());
        doNothing().when(manager1).setPhoneNo((String) any());
        doNothing().when(manager1).setRegistrationDate((Date) any());
        manager1.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setLastModified(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        manager1.setLicenseNo("License No");
        manager1.setManagerId(123L);
        manager1.setName("Name");
        manager1.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setRegistrationDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        assertThrows(ResourceException.class, () -> managerServiceImplementation.approvePendingRequestById(123L));
        verify(managerRepository).findById((Long) any());
        verify(manager1).setApprovalStatus((ApprovalStatus) any());
        verify(manager1).setLastModified((Date) any());
        verify(manager1).setLicenseNo((String) any());
        verify(manager1).setManagerId((Long) any());
        verify(manager1).setName((String) any());
        verify(manager1).setPhoneNo((String) any());
        verify(manager1).setRegistrationDate((Date) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#approvePendingRequestById(Long)}
     */
    @Test
    void testApprovePendingRequestById4() {
        Manager manager = mock(Manager.class);
        when(manager.getApprovalStatus()).thenReturn(ApprovalStatus.PENDING);
        doNothing().when(manager).setApprovalStatus((ApprovalStatus) any());
        doNothing().when(manager).setLastModified((Date) any());
        doNothing().when(manager).setLicenseNo((String) any());
        doNothing().when(manager).setManagerId((Long) any());
        doNothing().when(manager).setName((String) any());
        doNothing().when(manager).setPhoneNo((String) any());
        doNothing().when(manager).setRegistrationDate((Date) any());
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Manager> ofResult = Optional.of(manager);
        when(managerRepository.save((Manager) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        when(managerRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> managerServiceImplementation.approvePendingRequestById(123L));
        verify(managerRepository).save((Manager) any());
        verify(managerRepository).findById((Long) any());
        verify(manager).getApprovalStatus();
        verify(manager, atLeast(1)).setApprovalStatus((ApprovalStatus) any());
        verify(manager, atLeast(1)).setLastModified((Date) any());
        verify(manager).setLicenseNo((String) any());
        verify(manager).setManagerId((Long) any());
        verify(manager).setName((String) any());
        verify(manager).setPhoneNo((String) any());
        verify(manager).setRegistrationDate((Date) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#updateManager(ManagerDto)}
     */
    @Test
    void testUpdateManager() {
        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Manager> ofResult = Optional.of(manager);

        Manager manager1 = new Manager();
        manager1.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setLastModified(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        manager1.setLicenseNo("License No");
        manager1.setManagerId(123L);
        manager1.setName("Name");
        manager1.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setRegistrationDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        when(managerRepository.save((Manager) any())).thenReturn(manager1);
        when(managerRepository.findById((Long) any())).thenReturn(ofResult);

        Manager manager2 = new Manager();
        manager2.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager2.setLastModified(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        manager2.setLicenseNo("License No");
        manager2.setManagerId(123L);
        manager2.setName("Name");
        manager2.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager2.setRegistrationDate(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        when(modelMapper.map((Object) any(), (Class<Manager>) any())).thenReturn(manager2);
        assertThrows(ResourceException.class, () -> managerServiceImplementation.updateManager(new ManagerDto()));
        verify(managerRepository).save((Manager) any());
        verify(managerRepository).findById((Long) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#updateManager(ManagerDto)}
     */
    @Test
    void testUpdateManager2() {
        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        Optional<Manager> ofResult = Optional.of(manager);
        when(managerRepository.save((Manager) any()))
                .thenThrow(new ResourceException("Manager", "Manager", "Field Value", ResourceException.ErrorType.CREATED));
        when(managerRepository.findById((Long) any())).thenReturn(ofResult);

        Manager manager1 = new Manager();
        manager1.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setLastModified(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        manager1.setLicenseNo("License No");
        manager1.setManagerId(123L);
        manager1.setName("Name");
        manager1.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setRegistrationDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        when(modelMapper.map((Object) any(), (Class<Manager>) any())).thenReturn(manager1);
        assertThrows(ResourceException.class, () -> managerServiceImplementation.updateManager(new ManagerDto()));
        verify(managerRepository).save((Manager) any());
        verify(managerRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Manager>) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#updateManager(ManagerDto)}
     */
    @Test
    void testUpdateManager3() {
        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        when(managerRepository.save((Manager) any())).thenReturn(manager);
        when(managerRepository.findById((Long) any())).thenReturn(Optional.empty());

        Manager manager1 = new Manager();
        manager1.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setLastModified(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        manager1.setLicenseNo("License No");
        manager1.setManagerId(123L);
        manager1.setName("Name");
        manager1.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setRegistrationDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        when(modelMapper.map((Object) any(), (Class<Manager>) any())).thenReturn(manager1);
        assertThrows(ResourceException.class, () -> managerServiceImplementation.updateManager(new ManagerDto()));
        verify(managerRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Manager>) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#deleteManager(Long)}
     */
    @Test
    void testDeleteManager() {
        doNothing().when(managerRepository).deleteById((Long) any());
        when(managerRepository.existsById((Long) any())).thenReturn(true);
        managerServiceImplementation.deleteManager(123L);
        verify(managerRepository).existsById((Long) any());
        verify(managerRepository).deleteById((Long) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#deleteManager(Long)}
     */
    @Test
    void testDeleteManager2() {
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(managerRepository)
                .deleteById((Long) any());
        when(managerRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(ResourceException.class, () -> managerServiceImplementation.deleteManager(123L));
        verify(managerRepository).existsById((Long) any());
        verify(managerRepository).deleteById((Long) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#deleteManager(Long)}
     */
    @Test
    void testDeleteManager3() {
        doNothing().when(managerRepository).deleteById((Long) any());
        when(managerRepository.existsById((Long) any())).thenReturn(false);
        assertThrows(ResourceException.class, () -> managerServiceImplementation.deleteManager(123L));
        verify(managerRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#getManagersWithFilter(String, Integer, Integer, String, String)}
     */
    @Test
    void testGetManagersWithFilter() {
        ArrayList<Manager> managerList = new ArrayList<>();
        when(managerRepository.findAllByApprovalStatusAndNameContainingIgnoreCase((ApprovalStatus) any(), (String) any(),
                (Pageable) any())).thenReturn(new PageImpl<>(managerList));
        PageableResponse<ManagerDto> actualManagersWithFilter = managerServiceImplementation
                .getManagersWithFilter("Status", 10, 3, "Sort By", "Name");
        assertEquals(managerList, actualManagersWithFilter.getData());
        assertEquals(0L, actualManagersWithFilter.getTotalRecords().longValue());
        assertEquals(1, actualManagersWithFilter.getTotalPages().intValue());
        assertEquals(0, actualManagersWithFilter.getPageSize().intValue());
        assertEquals(0, actualManagersWithFilter.getPageNumber().intValue());
        assertTrue(actualManagersWithFilter.getIsLastPage());
        verify(managerRepository).findAllByApprovalStatusAndNameContainingIgnoreCase((ApprovalStatus) any(),
                (String) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link ManagerServiceImplementation#getManagersWithFilter(String, Integer, Integer, String, String)}
     */
    @Test
    void testGetManagersWithFilter2() {
        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("PENDING");
        manager.setManagerId(123L);
        manager.setName("PENDING");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<Manager> managerList = new ArrayList<>();
        managerList.add(manager);
        PageImpl<Manager> pageImpl = new PageImpl<>(managerList);
        when(managerRepository.findAllByApprovalStatusAndNameContainingIgnoreCase((ApprovalStatus) any(), (String) any(),
                (Pageable) any())).thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ManagerDto>) any())).thenReturn(new ManagerDto());
        PageableResponse<ManagerDto> actualManagersWithFilter = managerServiceImplementation
                .getManagersWithFilter("Status", 10, 3, "Sort By", "Name");
        assertEquals(1, actualManagersWithFilter.getData().size());
        assertEquals(1L, actualManagersWithFilter.getTotalRecords().longValue());
        assertEquals(1, actualManagersWithFilter.getTotalPages().intValue());
        assertEquals(1, actualManagersWithFilter.getPageSize().intValue());
        assertEquals(0, actualManagersWithFilter.getPageNumber().intValue());
        assertTrue(actualManagersWithFilter.getIsLastPage());
        verify(managerRepository).findAllByApprovalStatusAndNameContainingIgnoreCase((ApprovalStatus) any(),
                (String) any(), (Pageable) any());
        verify(modelMapper).map((Object) any(), (Class<ManagerDto>) any());
    }

}

