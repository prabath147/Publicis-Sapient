package com.admin.service.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.admin.service.dto.SubscriberDto;
import com.admin.service.service.SubscriberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {SubscriberController.class})
@ExtendWith(SpringExtension.class)
class SubscriberControllerTest {
    @Autowired
    private SubscriberController subscriberController;

    @MockBean
    private SubscriberService subscriberService;


    @Test
    void testRegisterUserForSubscription() throws Exception {
        when(subscriberService.registerUserForSubscription((Long) any(), (Long) any())).thenReturn(new SubscriberDto());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .post("/admin/subscriber/subscribe/{user-id}", 1L)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString(1L));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(subscriberController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"userId\":null,\"userSubsSet\":[]}"));
    }


    @Test
    void testDeleteSubscriber() throws Exception {
        doNothing().when(subscriberService).deleteSubscriber((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/admin/subscriber/delete/{id}", 123L);
        MockMvcBuilders.standaloneSetup(subscriberController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Deleted Subscriber Successfully!"));
    }


    @Test
    void testDeleteSubscriber2() throws Exception {
        doNothing().when(subscriberService).deleteSubscriber((Long) any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/admin/subscriber/delete/{id}", 123L);
        deleteResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(subscriberController)
                .build()
                .perform(deleteResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Deleted Subscriber Successfully!"));
    }


    @Test
    void testDeleteSubscriptionForSubscriber() throws Exception {
        doNothing().when(subscriberService).removeSubscription((Long) any(), (Long) any());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders
                .put("/admin/subscriber/unsubscribe/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString(1L));
        MockMvcBuilders.standaloneSetup(subscriberController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Deleted Subscription Successfully!"));
    }


    @Test
    void testGetSubscriberBySubscriberId() throws Exception {
        when(subscriberService.getSubscriber((Long) any())).thenReturn(new SubscriberDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/admin/subscriber/get-subscriber/{user-id}", 1L);
        MockMvcBuilders.standaloneSetup(subscriberController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"userId\":null,\"userSubsSet\":[]}"));
    }


}
