package com.admin.service.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.admin.service.client.pharmacy.StoreClient;
import com.admin.service.controller.AdminStoreController;
import com.admin.service.dto.AddressDto;
import com.admin.service.dto.ManagerDto;
import com.admin.service.dto.PageableResponse;
import com.admin.service.dto.StoreDto;
import com.admin.service.entity.ApprovalStatus;
import com.admin.service.exception.ResourceException;
import com.admin.service.service.AdminStoreService;
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

import java.util.Date;

@ContextConfiguration(classes = {AdminStoreServiceImpl.class})
@ExtendWith(SpringExtension.class)
class AdminStoreServiceImplTest {
    @Autowired
    private AdminStoreServiceImpl adminStoreServiceImpl;

    @MockBean
    private WebClient.Builder builder;

    @Autowired
    private AdminStoreService adminStoreService;

    @MockBean
    private StoreClient storeClient;


    @Test
    void testGetAllStores() {
        PageableResponse<StoreDto> pageableResponse = new PageableResponse<>();
        when(storeClient.getStores((Integer) any(), (Integer) any())).thenReturn(pageableResponse);
        assertSame(pageableResponse, adminStoreServiceImpl.getAllStores(10, 3));
        verify(storeClient).getStores((Integer) any(), (Integer) any());
    }


    @Test
    void testGetAllStores2() {
        when(storeClient.getStores((Integer) any(), (Integer) any()))
                .thenThrow(new RuntimeException("An error occurred"));
        assertThrows(RuntimeException.class, () -> adminStoreServiceImpl.getAllStores(10, 3));
        verify(storeClient).getStores((Integer) any(), (Integer) any());
    }


    @Test
    void testGetStoreByStoreId() {
        StoreDto store = new StoreDto();
        when(storeClient.getStoreById((Long) any())).thenReturn(store);
        assertSame(store, adminStoreServiceImpl.getStoreByStoreId(123L));
        verify(storeClient).getStoreById((Long) any());
    }


    @Test
    void testGetStoreByStoreId2() {
        StoreDto storeDto = new StoreDto();
        when(storeClient.getStoreById((Long) any())).thenReturn(storeDto);
        assertSame(storeDto, adminStoreServiceImpl.getStoreByStoreId(123L));
        verify(storeClient).getStoreById((Long) any());
    }

    @Test
    void testGetStoreByStoreId3() {
        when(storeClient.getStoreById((Long) any())).thenThrow(new RuntimeException("An error occurred"));
        assertThrows(ResourceException.class, () -> adminStoreServiceImpl.getStoreByStoreId(123L));
        verify(storeClient).getStoreById((Long) any());
    }


    @Test
    void testUpdateStore() {
        StoreDto store = new StoreDto();
        when(storeClient.updateStore((StoreDto) any())).thenReturn(store);
        assertSame(store, adminStoreServiceImpl.updateStore(new StoreDto()));
        verify(storeClient).updateStore((StoreDto) any());
    }


    @Test
    void testUpdateStore2() {
        StoreDto storeDto = new StoreDto();
        when(storeClient.updateStore((StoreDto) any())).thenReturn(storeDto);
        assertSame(storeDto, adminStoreServiceImpl.updateStore(new StoreDto()));
        verify(storeClient).updateStore((StoreDto) any());
    }


    @Test
    void testUpdateStore3() {
        when(storeClient.updateStore((StoreDto) any())).thenThrow(new RuntimeException("An error occurred"));
        assertThrows(ResourceException.class, () -> adminStoreServiceImpl.updateStore(new StoreDto()));
        verify(storeClient).updateStore((StoreDto) any());
    }



    @Test
    void testUpdateStore5() {
        when(storeClient.updateStore((StoreDto) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> adminStoreServiceImpl.updateStore(new StoreDto()));
        verify(storeClient).updateStore((StoreDto) any());
    }


    @Test
    void testDeleteStore() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.CONTINUE);
        when(storeClient.deleteStore((Long) any())).thenReturn(responseEntity);
        assertSame(responseEntity, adminStoreServiceImpl.deleteStore(123L));
        verify(storeClient).deleteStore((Long) any());
    }

    @Test
    void testDeleteStore2() {
        when(storeClient.deleteStore((Long) any())).thenThrow(new RuntimeException("An error occurred"));
        assertThrows(ResourceException.class, () -> adminStoreServiceImpl.deleteStore(123L));
        verify(storeClient).deleteStore((Long) any());
    }

    @Test
    void getStoreByStoreId() throws Exception{
        StoreDto store=(new StoreDto(61L,"Store1",
                new AddressDto(64L,"Street1","City1","State1",123456,"India"),
                new Date(),new ManagerDto(21L, "name1", "123456", "789", new Date(), ApprovalStatus.PENDING),1000.0));

        when(adminStoreService.getStoreByStoreId(1L)).thenReturn(store);
        StoreDto responseEntity = adminStoreService.getStoreByStoreId(1L);
        assertEquals(store, responseEntity);

    }
    @Test
    void getStoreByStoreId2() throws Exception{
        when(storeClient.getStoreById((Long) any())).thenThrow(new RuntimeException("An error occurred"));
        assertThrows(ResourceException.class, () -> adminStoreServiceImpl.getStoreByStoreId(123L));
        verify(storeClient).getStoreById((Long) any());
    }

    @Test
    void updateStoreByStoreId() throws  Exception{

        StoreDto store=(new StoreDto(61L,"Store1",
                new AddressDto(64L,"Street1","City1","State1",123456,"India"),
                new Date(),new ManagerDto(21L, "name1", "123456", "789", new Date(), ApprovalStatus.PENDING),1000.0));

        when(adminStoreService.updateStore(store)).thenReturn(store);
        StoreDto responseEntity=adminStoreService.updateStore(store);
        assertEquals(responseEntity, store);
    }
}


