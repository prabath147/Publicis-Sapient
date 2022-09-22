package com.sapient.notify.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.sapient.notify.client.auth.AuthClient;
import com.sapient.notify.dto.EmailDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSendException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {MailService.class})
@ExtendWith(SpringExtension.class)
class MailServiceTest {
    @MockBean
    private AuthClient authClient;

    @MockBean
    private AmazonSimpleEmailService amazonSimpleEmailService;

    @Autowired
    private MailService mailService;

@Test
void testSendEmail() throws MessagingException {
    when(amazonSimpleEmailService.sendEmail((SendEmailRequest) any())).thenReturn(new SendEmailResult());

    EmailDto emailDto = new EmailDto();
    emailDto.setEmailBody("Not all who wander are lost");
    emailDto.setEmailSubject("Hello from the Dreaming Spires");
    emailDto.setToEmail("jane.doe@example.org");
    assertEquals("Email has been sent successfully.", mailService.sendEmail(emailDto));
    verify(amazonSimpleEmailService).sendEmail((SendEmailRequest) any());
}
    /**
     * Method under test: {@link MailService#sendMessage(EmailDto)}
     */
    @Test
    void testSendMessage() throws MessagingException {
        when(amazonSimpleEmailService.sendEmail((SendEmailRequest) any())).thenReturn(new SendEmailResult());

        EmailDto emailDto = new EmailDto();
        emailDto.setEmailBody("Not all who wander are lost");
        emailDto.setEmailSubject("Hello from the Dreaming Spires");
        emailDto.setToEmail("jane.doe@example.org");
        assertEquals("Email has been sent successfully.", mailService.sendMessage(emailDto));
        verify(amazonSimpleEmailService).sendEmail((SendEmailRequest) any());
    }

    @Test
    void testSendMessageNotSent() throws Exception {

        // mock data and method behaviour
        EmailDto mockEmail = EmailDto.builder()
                .toEmail("abc@gmail.com")
                .userId(1L)
                .emailSubject("test")
                .emailBody("test message to abc")
                .build();
        when(amazonSimpleEmailService.sendEmail((SendEmailRequest) any())).thenThrow(new MailSendException("mail not sent."));

        assertEquals("Email was not sent.",mailService.sendMessage(mockEmail));
    }
}

