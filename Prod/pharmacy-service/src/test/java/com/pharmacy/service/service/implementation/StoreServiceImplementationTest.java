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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.dto.StoreDto;
import com.pharmacy.service.exception.ResourceException;
import com.pharmacy.service.model.Address;
import com.pharmacy.service.model.ApprovalStatus;
import com.pharmacy.service.model.Manager;
import com.pharmacy.service.model.Store;
import com.pharmacy.service.repository.StoreRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {StoreServiceImplementation.class})
@ExtendWith(SpringExtension.class)
class StoreServiceImplementationTest {
    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private StoreRepository storeRepository;

    @Autowired
    private StoreServiceImplementation storeServiceImplementation;

    /**
     * Method under test: {@link StoreServiceImplementation#getStore(Long)}
     */
    @Test
    void testGetStore() {
        StoreDto storeDto = new StoreDto();
        when(modelMapper.map((Object) any(), (Class<StoreDto>) any())).thenReturn(storeDto);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

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

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");
        Optional<Store> ofResult = Optional.of(store);
        when(storeRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(storeDto, storeServiceImplementation.getStore(123L));
        verify(modelMapper).map((Object) any(), (Class<StoreDto>) any());
        verify(storeRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#getStore(Long)}
     */
    @Test
    void testGetStore2() {
        when(modelMapper.map((Object) any(), (Class<StoreDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

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

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");
        Optional<Store> ofResult = Optional.of(store);
        when(storeRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> storeServiceImplementation.getStore(123L));
        verify(modelMapper).map((Object) any(), (Class<StoreDto>) any());
        verify(storeRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#noOfStores()}
     */
    @Test
    void testNoOfStores() {
        when(storeRepository.count()).thenReturn(3L);
        assertEquals(3L, storeServiceImplementation.noOfStores().longValue());
        verify(storeRepository).count();
    }

    /**
     * Method under test: {@link StoreServiceImplementation#noOfStores()}
     */
    @Test
    void testNoOfStores2() {
        when(storeRepository.count()).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> storeServiceImplementation.noOfStores());
        verify(storeRepository).count();
    }

    /**
     * Method under test: {@link StoreServiceImplementation#noOfStores(Long)}
     */
    @Test
    void testNoOfStores3() {
        when(storeRepository.countAllByManager_ManagerId((Long) any())).thenReturn(3L);
        assertEquals(3L, storeServiceImplementation.noOfStores(123L).longValue());
        verify(storeRepository).countAllByManager_ManagerId((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#noOfStores(Long)}
     */
    @Test
    void testNoOfStores4() {
        when(storeRepository.countAllByManager_ManagerId((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> storeServiceImplementation.noOfStores(123L));
        verify(storeRepository).countAllByManager_ManagerId((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#totalRevenue(Long)}
     */
    @Test
    void testTotalRevenue() {
        when(storeRepository.findAllByManager_ManagerId((Long) any())).thenReturn(new ArrayList<>());
        assertEquals(0.0d, storeServiceImplementation.totalRevenue(123L).doubleValue());
        verify(storeRepository).findAllByManager_ManagerId((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#totalRevenue(Long)}
     */
    @Test
    void testTotalRevenue2() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

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

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        ArrayList<Store> storeList = new ArrayList<>();
        storeList.add(store);
        when(storeRepository.findAllByManager_ManagerId((Long) any())).thenReturn(storeList);
        assertEquals(10.0d, storeServiceImplementation.totalRevenue(123L).doubleValue());
        verify(storeRepository).findAllByManager_ManagerId((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#totalRevenue(Long)}
     */
    @Test
    void testTotalRevenue3() {
        when(storeRepository.findAllByManager_ManagerId((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> storeServiceImplementation.totalRevenue(123L));
        verify(storeRepository).findAllByManager_ManagerId((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#getStoreAsync(Long)}
     */
    @Test
    void testGetStoreAsync() {
        when(modelMapper.map((Object) any(), (Class<StoreDto>) any())).thenReturn(new StoreDto());

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

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

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");
        Optional<Store> ofResult = Optional.of(store);
        when(storeRepository.findById((Long) any())).thenReturn(ofResult);
        storeServiceImplementation.getStoreAsync(123L);
        verify(modelMapper).map((Object) any(), (Class<StoreDto>) any());
        verify(storeRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#getStoreAsync(Long)}
     */
    @Test
    void testGetStoreAsync2() {
        when(modelMapper.map((Object) any(), (Class<StoreDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

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

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");
        Optional<Store> ofResult = Optional.of(store);
        when(storeRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> storeServiceImplementation.getStoreAsync(123L));
        verify(modelMapper).map((Object) any(), (Class<StoreDto>) any());
        verify(storeRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#getStores(Integer, Integer)}
     */
    @Test
    void testGetStores() {
        ArrayList<Store> storeList = new ArrayList<>();
        when(storeRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(storeList));
        PageableResponse<StoreDto> actualStores = storeServiceImplementation.getStores(10, 3);
        assertEquals(storeList, actualStores.getData());
        assertEquals(0L, actualStores.getTotalRecords().longValue());
        assertEquals(1, actualStores.getTotalPages().intValue());
        assertEquals(0, actualStores.getPageSize().intValue());
        assertEquals(0, actualStores.getPageNumber().intValue());
        assertTrue(actualStores.getIsLastPage());
        verify(storeRepository).findAll((Pageable) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#getStores(Integer, Integer)}
     */
    @Test
    void testGetStores2() {
        when(modelMapper.map((Object) any(), (Class<StoreDto>) any())).thenReturn(new StoreDto());

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

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

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        ArrayList<Store> storeList = new ArrayList<>();
        storeList.add(store);
        PageImpl<Store> pageImpl = new PageImpl<>(storeList);
        when(storeRepository.findAll((Pageable) any())).thenReturn(pageImpl);
        PageableResponse<StoreDto> actualStores = storeServiceImplementation.getStores(10, 3);
        assertEquals(1, actualStores.getData().size());
        assertEquals(1L, actualStores.getTotalRecords().longValue());
        assertEquals(1, actualStores.getTotalPages().intValue());
        assertEquals(1, actualStores.getPageSize().intValue());
        assertEquals(0, actualStores.getPageNumber().intValue());
        assertTrue(actualStores.getIsLastPage());
        verify(modelMapper).map((Object) any(), (Class<StoreDto>) any());
        verify(storeRepository).findAll((Pageable) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists() {
        when(storeRepository.existsById((Long) any())).thenReturn(true);
        assertTrue(storeServiceImplementation.checkIfExists(123L));
        verify(storeRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists2() {
        when(storeRepository.existsById((Long) any())).thenReturn(false);
        assertFalse(storeServiceImplementation.checkIfExists(123L));
        verify(storeRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists3() {
        when(storeRepository.existsById((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> storeServiceImplementation.checkIfExists(123L));
        verify(storeRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#getManagerStore(Long, Integer, Integer)}
     */
    @Test
    void testGetManagerStore() {
        ArrayList<Store> storeList = new ArrayList<>();
        when(storeRepository.findAllByManager_ManagerId((Long) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(storeList));
        PageableResponse<StoreDto> actualManagerStore = storeServiceImplementation.getManagerStore(123L, 10, 3);
        assertEquals(storeList, actualManagerStore.getData());
        assertEquals(0L, actualManagerStore.getTotalRecords().longValue());
        assertEquals(1, actualManagerStore.getTotalPages().intValue());
        assertEquals(0, actualManagerStore.getPageSize().intValue());
        assertEquals(0, actualManagerStore.getPageNumber().intValue());
        assertTrue(actualManagerStore.getIsLastPage());
        verify(storeRepository).findAllByManager_ManagerId((Long) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#getManagerStore(Long, Integer, Integer)}
     */
    @Test
    void testGetManagerStore2() {
        when(modelMapper.map((Object) any(), (Class<StoreDto>) any())).thenReturn(new StoreDto());

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

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

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        ArrayList<Store> storeList = new ArrayList<>();
        storeList.add(store);
        PageImpl<Store> pageImpl = new PageImpl<>(storeList);
        when(storeRepository.findAllByManager_ManagerId((Long) any(), (Pageable) any())).thenReturn(pageImpl);
        PageableResponse<StoreDto> actualManagerStore = storeServiceImplementation.getManagerStore(123L, 10, 3);
        assertEquals(1, actualManagerStore.getData().size());
        assertEquals(1L, actualManagerStore.getTotalRecords().longValue());
        assertEquals(1, actualManagerStore.getTotalPages().intValue());
        assertEquals(1, actualManagerStore.getPageSize().intValue());
        assertEquals(0, actualManagerStore.getPageNumber().intValue());
        assertTrue(actualManagerStore.getIsLastPage());
        verify(modelMapper).map((Object) any(), (Class<StoreDto>) any());
        verify(storeRepository).findAllByManager_ManagerId((Long) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#getStoresByManagerToDelete(Long)}
     */
    @Test
    void testGetStoresByManagerToDelete() {
        ArrayList<Store> storeList = new ArrayList<>();
        when(storeRepository.findAllByManager_ManagerId((Long) any())).thenReturn(storeList);
        List<Store> actualStoresByManagerToDelete = storeServiceImplementation.getStoresByManagerToDelete(123L);
        assertSame(storeList, actualStoresByManagerToDelete);
        assertTrue(actualStoresByManagerToDelete.isEmpty());
        verify(storeRepository).findAllByManager_ManagerId((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#getStoresByManagerToDelete(Long)}
     */
    @Test
    void testGetStoresByManagerToDelete2() {
        when(storeRepository.findAllByManager_ManagerId((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> storeServiceImplementation.getStoresByManagerToDelete(123L));
        verify(storeRepository).findAllByManager_ManagerId((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#createStore(StoreDto)}
     */
    @Test
    void testCreateStore() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

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

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(store);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode(1);
        address1.setState("MD");
        address1.setStreet("Street");

        Manager manager1 = new Manager();
        manager1.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setLastModified(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        manager1.setLicenseNo("License No");
        manager1.setManagerId(123L);
        manager1.setName("Name");
        manager1.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setRegistrationDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));

        Store store1 = new Store();
        store1.setAddress(address1);
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store1.setCreatedDate(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        store1.setManager(manager1);
        store1.setRevenue(10.0d);
        store1.setStoreId(123L);
        store1.setStoreName("Store Name");
        when(storeRepository.save((Store) any())).thenReturn(store1);
        assertThrows(ResourceException.class, () -> storeServiceImplementation.createStore(new StoreDto()));
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
        verify(storeRepository).save((Store) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#updateStore(StoreDto)}
     */
    @Test
    void testUpdateStore() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

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

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");
        when(modelMapper.map((Object) any(), (Class<Store>) any())).thenReturn(store);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode(1);
        address1.setState("MD");
        address1.setStreet("Street");

        Manager manager1 = new Manager();
        manager1.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setLastModified(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        manager1.setLicenseNo("License No");
        manager1.setManagerId(123L);
        manager1.setName("Name");
        manager1.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setRegistrationDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));

        Store store1 = new Store();
        store1.setAddress(address1);
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store1.setCreatedDate(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        store1.setManager(manager1);
        store1.setRevenue(10.0d);
        store1.setStoreId(123L);
        store1.setStoreName("Store Name");
        Optional<Store> ofResult = Optional.of(store1);

        Address address2 = new Address();
        address2.setAddressId(123L);
        address2.setCity("Oxford");
        address2.setCountry("GB");
        address2.setPinCode(1);
        address2.setState("MD");
        address2.setStreet("Street");

        Manager manager2 = new Manager();
        manager2.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult6 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager2.setLastModified(Date.from(atStartOfDayResult6.atZone(ZoneId.of("UTC")).toInstant()));
        manager2.setLicenseNo("License No");
        manager2.setManagerId(123L);
        manager2.setName("Name");
        manager2.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult7 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager2.setRegistrationDate(Date.from(atStartOfDayResult7.atZone(ZoneId.of("UTC")).toInstant()));

        Store store2 = new Store();
        store2.setAddress(address2);
        LocalDateTime atStartOfDayResult8 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store2.setCreatedDate(Date.from(atStartOfDayResult8.atZone(ZoneId.of("UTC")).toInstant()));
        store2.setManager(manager2);
        store2.setRevenue(10.0d);
        store2.setStoreId(123L);
        store2.setStoreName("Store Name");
        when(storeRepository.save((Store) any())).thenReturn(store2);
        when(storeRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> storeServiceImplementation.updateStore(new StoreDto()));
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
        verify(storeRepository).save((Store) any());
        verify(storeRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#updateStore(StoreDto)}
     */
    @Test
    void testUpdateStore2() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

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

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");
        when(modelMapper.map((Object) any(), (Class<Store>) any())).thenReturn(store);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode(1);
        address1.setState("MD");
        address1.setStreet("Street");

        Manager manager1 = new Manager();
        manager1.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setLastModified(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        manager1.setLicenseNo("License No");
        manager1.setManagerId(123L);
        manager1.setName("Name");
        manager1.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setRegistrationDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));

        Store store1 = new Store();
        store1.setAddress(address1);
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store1.setCreatedDate(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        store1.setManager(manager1);
        store1.setRevenue(10.0d);
        store1.setStoreId(123L);
        store1.setStoreName("Store Name");
        Optional<Store> ofResult = Optional.of(store1);
        when(storeRepository.save((Store) any()))
                .thenThrow(new ResourceException("Store", "Store", "Field Value", ResourceException.ErrorType.CREATED));
        when(storeRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> storeServiceImplementation.updateStore(new StoreDto()));
        verify(modelMapper).map((Object) any(), (Class<Store>) any());
        verify(storeRepository).save((Store) any());
        verify(storeRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#updateStore(Set)}
     */
    @Test
    void testUpdateStore3() {
        when(storeRepository.saveAll((Iterable<Store>) any())).thenReturn(new ArrayList<>());
        storeServiceImplementation.updateStore(new HashSet<>());
        verify(storeRepository).saveAll((Iterable<Store>) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#updateStore(Set)}
     */
    @Test
    void testUpdateStore4() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

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

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");
        when(modelMapper.map((Object) any(), (Class<Store>) any())).thenReturn(store);
        when(storeRepository.saveAll((Iterable<Store>) any())).thenReturn(new ArrayList<>());

        HashSet<StoreDto> storeDtoSet = new HashSet<>();
        storeDtoSet.add(new StoreDto());
        storeServiceImplementation.updateStore(storeDtoSet);
        verify(modelMapper).map((Object) any(), (Class<Store>) any());
        verify(storeRepository).saveAll((Iterable<Store>) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#updateStore(Set)}
     */
    @Test
    void testUpdateStore5() {
        when(modelMapper.map((Object) any(), (Class<Store>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        when(storeRepository.saveAll((Iterable<Store>) any())).thenReturn(new ArrayList<>());

        HashSet<StoreDto> storeDtoSet = new HashSet<>();
        storeDtoSet.add(new StoreDto());
        assertThrows(ResourceException.class, () -> storeServiceImplementation.updateStore(storeDtoSet));
        verify(modelMapper).map((Object) any(), (Class<Store>) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#deleteStore(Long)}
     */
    @Test
    void testDeleteStore() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

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

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");
        Optional<Store> ofResult = Optional.of(store);
        doNothing().when(storeRepository).deleteById((Long) any());
        when(storeRepository.findById((Long) any())).thenReturn(ofResult);
        storeServiceImplementation.deleteStore(123L);
        verify(storeRepository).findById((Long) any());
        verify(storeRepository).deleteById((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#deleteStore(Long)}
     */
    @Test
    void testDeleteStore2() {
        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

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

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");
        Optional<Store> ofResult = Optional.of(store);
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(storeRepository)
                .deleteById((Long) any());
        when(storeRepository.findById((Long) any())).thenReturn(ofResult);
        assertThrows(ResourceException.class, () -> storeServiceImplementation.deleteStore(123L));
        verify(storeRepository).findById((Long) any());
        verify(storeRepository).deleteById((Long) any());
    }

    /**
     * Method under test: {@link StoreServiceImplementation#deleteStore(Long)}
     */
    @Test
    void testDeleteStore3() {
        doNothing().when(storeRepository).deleteById((Long) any());
        when(storeRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertThrows(ResourceException.class, () -> storeServiceImplementation.deleteStore(123L));
        verify(storeRepository).findById((Long) any());
    }
}

