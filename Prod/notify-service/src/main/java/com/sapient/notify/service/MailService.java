package com.sapient.notify.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.sapient.notify.client.auth.AuthClient;
import com.sapient.notify.dto.EmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@Slf4j
public class MailService {

    @Autowired
    AuthClient authClient;

    @Autowired
    AmazonSimpleEmailService amazonSimpleEmailService;

    public String sendEmail(EmailDto emailDto) throws MessagingException {
        return sendMessage(emailDto);
    }
    public String sendMessage(EmailDto emailDto) throws MessagingException {
        String senderEmail = "avinash.kumar3@publicissapient.com";
        String receiverEmail = emailDto.getToEmail();
        Long id = emailDto.getUserId();
        String emailSubject = emailDto.getEmailSubject();
        String body = emailDto.getEmailBody();
        try {
            SendEmailRequest sendEmailRequest =
                    new SendEmailRequest()
                            .withDestination(new Destination().withToAddresses(receiverEmail))
                            .withMessage(
                                    new Message()
                                            .withBody(
                                                    new Body()
                                                            .withHtml(new Content().withCharset("UTF-8").withData(body)))
                                            .withSubject(new Content().withCharset("UTF-8").withData(emailSubject)))
                            .withSource(senderEmail);
            SendEmailResult result = amazonSimpleEmailService.sendEmail(sendEmailRequest);
            log.info(result.getMessageId());
            return "Email has been sent successfully.";
        } catch (Exception e) {
            log.info(String.valueOf(e));
            return "Email was not sent.";
        }
    }
}
