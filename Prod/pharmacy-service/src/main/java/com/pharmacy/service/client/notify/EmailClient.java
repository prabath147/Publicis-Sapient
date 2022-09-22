package com.pharmacy.service.client.notify;

import com.pharmacy.service.dtoexternal.EmailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(
        name = "email",
        url = "${notifyServiceURL}/mail"
)
public interface EmailClient {
    @PostMapping("/send")
    ResponseEntity<String> sendBulkEmail(List<EmailDto> emailDtoList);
}
