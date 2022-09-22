package com.sapient.notify.controller;

import com.sapient.notify.client.auth.AuthClient;
import com.sapient.notify.dto.EmailDto;
import com.sapient.notify.exception.ResourceException;
import com.sapient.notify.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notify/mail")
public class EmailController {
    @Autowired
    private MailService mailService;

    @Autowired
    AuthClient authClient;

    @PostMapping("/send")
    public void sendEmail(@RequestBody List<EmailDto> emailDtoList) throws MessagingException {
        List<Long> emails = new ArrayList<>();
        for(EmailDto email: emailDtoList){
            emails.add(email.getUserId());
        }
        Optional<List<String>> optionalEmailList  = Optional.ofNullable(authClient.getEmails(emails).getBody());
        if(optionalEmailList.isEmpty())
            throw new ResourceException("Email","email",emailDtoList, ResourceException.ErrorType.FOUND);

        List<String> emailList = optionalEmailList.get();
        for(int i=0; i<emailDtoList.size();i++){
            EmailDto emailDto = emailDtoList.get(i);
            emailDto.setToEmail(emailList.get(i));
            mailService.sendEmail(emailDto);
        }
    }
}
