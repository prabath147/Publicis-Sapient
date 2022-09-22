package com.pharmacy.authservice.client.notify;

import com.pharmacy.authservice.payload.request.EmailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "email",
        url = "${emailServiceUrl}/mail"
)
public interface EmailClient {

    @PostMapping("/send")
    public void sendEmail(@RequestHeader("Authorization") String token, @RequestBody List<EmailDto> emailDtoList);
}
