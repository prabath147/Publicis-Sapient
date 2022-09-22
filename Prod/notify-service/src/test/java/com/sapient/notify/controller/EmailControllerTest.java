package com.sapient.notify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.notify.client.auth.AuthClient;
import com.sapient.notify.dto.EmailDto;
import com.sapient.notify.service.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {EmailController.class})
@ExtendWith(SpringExtension.class)
class EmailControllerTest {
    @MockBean
    private AuthClient authClient;

    @Autowired
    private EmailController emailController;

    @MockBean
    private MailService mailService;

    ResponseEntity<List<String>> mockEmailListResponse;

    List<EmailDto> emailDtoList;

    @BeforeEach
    void setup(){
        // create mock data for test
        List<String> mockEmailList = List.of("abc@gmail.com", "test@gmail.com");
        mockEmailListResponse = ResponseEntity.ok().body(mockEmailList);

        emailDtoList = new ArrayList<>();
        emailDtoList.add(EmailDto.builder()
                        .toEmail("abc@gmail.com")
                        .userId(1L)
                        .emailSubject("test")
                        .emailBody("test message to abc")
                        .build());

        emailDtoList.add(EmailDto.builder()
                .toEmail("test@gmail.com")
                .userId(2L)
                .emailSubject("test")
                .emailBody("test message to test")
                .build());
    }

    @Test
    void testSendEmailList() throws Exception{
        when(authClient.getEmails(anyList())).thenReturn(mockEmailListResponse);

        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/notify/mail/send")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(emailDtoList));
        MockMvcBuilders.standaloneSetup(emailController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void testSendEmailListIsEmpty() throws Exception{
        ResponseEntity<List<String>> mockResponse = ResponseEntity.of(Optional.empty());
        when(authClient.getEmails(anyList())).thenReturn(mockResponse);

        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/notify/mail/send")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(emailDtoList));
        MockMvcBuilders.standaloneSetup(emailController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().is5xxServerError());
    }
}

