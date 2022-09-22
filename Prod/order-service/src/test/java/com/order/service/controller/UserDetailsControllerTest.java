
package com.order.service.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.service.dto.AddressDto;
import com.order.service.dto.UserDetailsDto;
import com.order.service.service.UserDetailsService;
import com.order.service.utils.JwtUtils;
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

@ContextConfiguration(classes = {UserDetailsController.class})
@ExtendWith(SpringExtension.class)
class UserDetailsControllerTest {
    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsController userDetailsController;

    @MockBean
    private UserDetailsService userDetailsService;

    
    @Test
    void testGetUserDetails() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(userDetailsService.getUserDetails((Long) any())).thenReturn(new UserDetailsDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/order/user-details/get-user-details/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(userDetailsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"userId\":null,\"fullName\":null,\"mobileNumber\":null,\"address\":null}"));
    }

    
    @Test
    void testGetUserDetails2() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(userDetailsService.getUserDetails((Long) any())).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/order/user-details/get-user-details/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userDetailsController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

   
    @Test
    void testGetUserDetails3() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        when(userDetailsService.getUserDetails((Long) any())).thenReturn(new UserDetailsDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/order/user-details/get-user-details/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userDetailsController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(401));
    }

   
    @Test
    void testCreateUserDetails() throws Exception {
        when(userDetailsService.createUserDetails((Long) any(), (UserDetailsDto) any())).thenReturn(new UserDetailsDto());

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setAddress(new AddressDto());
        userDetailsDto.setFullName("Dr Jane Doe");
        userDetailsDto.setMobileNumber("42");
        userDetailsDto.setUserId(123L);
        String content = (new ObjectMapper()).writeValueAsString(userDetailsDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/order/user-details/create-user-details/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userDetailsController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"userId\":null,\"fullName\":null,\"mobileNumber\":null,\"address\":null}"));
    }

    
    @Test
    void testCreateUserDetails2() throws Exception {
        when(userDetailsService.createUserDetails((Long) any(), (UserDetailsDto) any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setAddress(new AddressDto());
        userDetailsDto.setFullName("Dr Jane Doe");
        userDetailsDto.setMobileNumber("42");
        userDetailsDto.setUserId(123L);
        String content = (new ObjectMapper()).writeValueAsString(userDetailsDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/order/user-details/create-user-details/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userDetailsController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    
    @Test
    void testUpdateUserDetails() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(userDetailsService.updateUserDetails((UserDetailsDto) any())).thenReturn(new UserDetailsDto());

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setAddress(new AddressDto());
        userDetailsDto.setFullName("Dr Jane Doe");
        userDetailsDto.setMobileNumber("42");
        userDetailsDto.setUserId(123L);
        String content = (new ObjectMapper()).writeValueAsString(userDetailsDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/order/user-details/update-user-details")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userDetailsController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"userId\":null,\"fullName\":null,\"mobileNumber\":null,\"address\":null}"));
    }

   
    @Test
    void testUpdateUserDetails2() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        when(userDetailsService.updateUserDetails((UserDetailsDto) any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setAddress(new AddressDto());
        userDetailsDto.setFullName("Dr Jane Doe");
        userDetailsDto.setMobileNumber("42");
        userDetailsDto.setUserId(123L);
        String content = (new ObjectMapper()).writeValueAsString(userDetailsDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/order/user-details/update-user-details")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userDetailsController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    
    @Test
    void testUpdateUserDetails3() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        when(userDetailsService.updateUserDetails((UserDetailsDto) any())).thenReturn(new UserDetailsDto());

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setAddress(new AddressDto());
        userDetailsDto.setFullName("Dr Jane Doe");
        userDetailsDto.setMobileNumber("42");
        userDetailsDto.setUserId(123L);
        String content = (new ObjectMapper()).writeValueAsString(userDetailsDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/order/user-details/update-user-details")
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userDetailsController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(401));
    }

   
    @Test
    void testDeleteUserDetails() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        doNothing().when(userDetailsService).deleteUserDetails((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/order/user-details/delete-user-details/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        MockMvcBuilders.standaloneSetup(userDetailsController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link UserDetailsController#deleteUserDetails(String, Long)}
     */
    @Test
    void testDeleteUserDetails2() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(true);
        doThrow(new ResponseStatusException(HttpStatus.CONTINUE)).when(userDetailsService)
                .deleteUserDetails((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/order/user-details/delete-user-details/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userDetailsController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link UserDetailsController#deleteUserDetails(String, Long)}
     */
    @Test
    void testDeleteUserDetails3() throws Exception {
        when(jwtUtils.verifyId((String) any(), (Long) any(), anyBoolean())).thenReturn(false);
        doNothing().when(userDetailsService).deleteUserDetails((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/order/user-details/delete-user-details/{id}", 123L)
                .header("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userDetailsController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(401));
    }
}


