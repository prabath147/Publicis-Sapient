//package com.admin.service.controller;
//
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//
//import com.admin.service.dto.PageableResponse;
//import com.admin.service.dto.SubscriptionsDto;
//import com.admin.service.service.SubscriptionService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//@ContextConfiguration(classes = {SubscriptionController.class})
//@ExtendWith(SpringExtension.class)
//class SubscriptionControllerTest {
//    @Autowired
//    private SubscriptionController subscriptionController;
//
//    @MockBean
//    private SubscriptionService subscriptionService;
//
//    @Test
//    void testGetSubscriptionById() throws Exception {
//        when(subscriptionService.getAllSubscriptions((Integer) any(), (Integer) any()))
//                .thenReturn(new PageableResponse<>());
//        when(subscriptionService.getSubscriptionById((Long) any())).thenReturn(new SubscriptionsDto());
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
//                .get("/admin/subscription/get-subscription/{subscription-id}", "", "Uri Variables");
//        MockMvcBuilders.standaloneSetup(subscriptionController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content()
//                        .string(
//                                "{\"data\":null,\"pageNumber\":null,\"pageSize\":null,\"totalRecords\":null,\"totalPages\":null,\"isLastPage"
//                                        + "\":null}"));
//    }
//
//    @Test
//    void testDeleteSubscription() throws Exception {
//        doNothing().when(subscriptionService).deleteSubscription((Long) any());
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
//                .delete("/admin/subscription/delete-subscription/{subscription-id}", 1L);
//        MockMvcBuilders.standaloneSetup(subscriptionController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
//                .andExpect(MockMvcResultMatchers.content().string("Deleted Successfully"));
//    }
//
//    @Test
//    void testDeleteSubscription2() throws Exception {
//        doNothing().when(subscriptionService).deleteSubscription((Long) any());
//        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders
//                .delete("/admin/subscription/delete-subscription/{subscription-id}", 1L);
//        deleteResult.characterEncoding("Encoding");
//        MockMvcBuilders.standaloneSetup(subscriptionController)
//                .build()
//                .perform(deleteResult)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
//                .andExpect(MockMvcResultMatchers.content().string("Deleted Successfully"));
//    }
//
//    @Test
//    void testGetSubscriptions() throws Exception {
//        when(subscriptionService.getAllSubscriptions((Integer) any(), (Integer) any()))
//                .thenReturn(new PageableResponse<>());
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/subscription/get-subscription");
//        MockMvcBuilders.standaloneSetup(subscriptionController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content()
//                        .string(
//                                "{\"data\":null,\"pageNumber\":null,\"pageSize\":null,\"totalRecords\":null,\"totalPages\":null,\"isLastPage"
//                                        + "\":null}"));
//    }
//}
package com.admin.service.controller;

import com.admin.service.dto.SubscriptionsDto;
import com.admin.service.entity.Benefits;
import com.admin.service.dto.PageableResponse;
import com.admin.service.entity.SubscriptionStatus;
import com.admin.service.service.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class SubscriptionControllerTest {
    @Autowired
    private SubscriptionController subscriptionController;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private SubscriptionService subscriptionService;


    SubscriptionsDto mockSubscriptionsDtoAfterExp;

    SubscriptionsDto mockSubscriptionsDtoBeforeExp;
    Benefits mockBenefits;


    @BeforeEach
    void setUp() throws Exception {
        mockBenefits = new Benefits();
        mockBenefits.setOne_day_delivery(false);
        mockBenefits.setDelivery_discount(10);


        mockSubscriptionsDtoAfterExp = SubscriptionsDto.builder()
                .subscriptionId(1L)
                .subscriptionName("Sub Name")
                .description("Desc For testing")
                .startDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-08-08 12:15:00"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-07-08 12:15:00"))
                .subscriptionCost(100.00)
                .benefits(mockBenefits)
                .status(SubscriptionStatus.EXPIRED)
                .period(15)
                .build();

        mockSubscriptionsDtoBeforeExp = SubscriptionsDto.builder()
                .subscriptionId(2L)
                .subscriptionName("Sub Name")
                .description("Desc For testing")
                .startDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2021-08-08 12:15:00"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-07-08 12:15:00"))
                .subscriptionCost(100.00)
                .benefits(mockBenefits)
                .status(SubscriptionStatus.ACTIVE)
                .period(15)
                .build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void getSubscriptionById() throws Exception {
        when(subscriptionService.getSubscriptionById(anyLong())).thenReturn(mockSubscriptionsDtoAfterExp);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/subscription/get-subscription/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subscriptionId").value(1L));
    }


    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void getSubscriptionByStatusActive() throws Exception {
        List<SubscriptionsDto> subscriptionsDtoList = new ArrayList<>();
        subscriptionsDtoList.add(mockSubscriptionsDtoAfterExp);
        PageableResponse<SubscriptionsDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setData(subscriptionsDtoList);

        when(subscriptionService.getAllSubscriptionsByStatus(0, 1, SubscriptionStatus.ACTIVE)).thenReturn(pageableResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/subscription/get-subscription/by-status")
                        .param("pageNumber", "0")
                        .param("pageSize", "1")
                        .param("status", "ACTIVE"))
                .andExpect(status().isOk());

        assert (pageableResponse.getData().toArray(new SubscriptionsDto[0]).length > 0);
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void getAllSubscriptions() throws Exception {
        List<SubscriptionsDto> subscriptionsDtoList = new ArrayList<>();
        subscriptionsDtoList.add(mockSubscriptionsDtoAfterExp);
        PageableResponse<SubscriptionsDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setData(subscriptionsDtoList);

        when(subscriptionService.getAllSubscriptions(0, 1)).thenReturn(pageableResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/subscription/get-subscription")
                .param("pageNumber", "0")
                .param("pageSize", "1")).andExpect(status().isOk());

        assert (pageableResponse.getData().toArray(new SubscriptionsDto[0]).length > 0);
    }


    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void createSubscription() throws Exception {
        when(subscriptionService.createSubscription(any(SubscriptionsDto.class))).thenReturn(mockSubscriptionsDtoAfterExp);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/subscription/create-subscription")
                        .content(new ObjectMapper().writeValueAsString(mockSubscriptionsDtoAfterExp)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subscriptionId").value(1L));
    }

    /**
     * Method under test: {@link SubscriptionController#deleteSubscription(Long)}
     */
    @Test
    void testDeleteSubscription() throws Exception {
        when(subscriptionService.deleteSubscription((Long) any())).thenReturn("Delete Subscription");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/admin/subscription/delete-subscription/{subscription-id}", 1L);
        MockMvcBuilders.standaloneSetup(subscriptionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Delete Subscription"));
    }

//    @Test
//    @WithMockUser(username = "user", roles = {"ADMIN"})
//    void deleteSubscriptionAfterExpiry() throws Exception {
//        when(subscriptionService.deleteSubscription(mockSubscriptionsDtoAfterExp.getSubscriptionId()).thenReturn("Deleted Successfully");
//        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/subscription/delete-subscription/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Deleted Successfully"));
//    }
}
