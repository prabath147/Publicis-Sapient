package com.admin.service.client.notify;

import com.admin.service.dto.EmailDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.List;
@FeignClient(
        name = "email",
        url = "${emailServiceURL}/mail"
)
public interface EmailClient {

    @PostMapping("/send")
    public void sendEmail( @RequestBody List<EmailDto> emailDtoList);
}
