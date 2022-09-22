package com.admin.service.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.admin.service.dto.*;
import com.admin.service.entity.ApprovalStatus;
import com.admin.service.service.AdminStoreService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.HashSet;

@ContextConfiguration(classes = {AdminStoreController.class})
@ExtendWith(SpringExtension.class)
class AdminStoreControllerTest {
    @Autowired
    private AdminStoreController adminStoreController;

    @MockBean
    private AdminStoreService adminStoreService;

    @Test
    void testDeleteStoreByStoreId() throws Exception {
        when(adminStoreService.deleteStore((Long) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/admin/store/{storeId}/delete", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(adminStoreController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Unable to delete Store 123 due to some error!!"));
    }

    @Test
    void testGetAllStores() throws Exception {
        when(adminStoreService.getAllStores((Integer) any(), (Integer) any())).thenReturn(new PageableResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/store");
        MockMvcBuilders.standaloneSetup(adminStoreController)
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
    void testGetStoreByStoreId() throws Exception {
        when(adminStoreService.getStoreByStoreId((Long) any())).thenReturn(new StoreDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/store/1");
        MockMvcBuilders.standaloneSetup(adminStoreController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link AdminStoreController#updateStoreByStoreId(StoreDto)}
     */
    @Test
    void testUpdateStoreByStoreId() throws Exception {
        when(adminStoreService.updateStore((StoreDto) any())).thenReturn(new StoreDto());

        StoreDto store=(new StoreDto(61L,"Store1",
                new AddressDto(64L,"Street1","City1","State1",123456,"India"),
                new Date(),new ManagerDto(21L, "name1", "123456", "789", new Date(), ApprovalStatus.PENDING),1000.0));

        String content = (new ObjectMapper()).writeValueAsString(store);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/admin/store/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(adminStoreController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));

    }

    @Test
    void testDeleteStoreByStoreId2() throws Exception {
        when(adminStoreService.deleteStore((Long) any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/admin/store/{storeId}/delete",
                123L);
        MockMvcBuilders.standaloneSetup(adminStoreController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
