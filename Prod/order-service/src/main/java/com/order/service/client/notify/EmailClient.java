package com.order.service.client.notify;

import com.order.service.dtoexternal.EmailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "email",
        url = "${notifyServiceURL}/mail"
)
public interface EmailClient {
    @PostMapping("/send")
    ResponseEntity<String> sendBulkEmail(List<EmailDto> emailDtoList);
    
    @PostMapping("/send")
    ResponseEntity<String> sendBulkEmail(@RequestHeader("Authorization") String bearerToken,List<EmailDto> emailDtoList);
}
