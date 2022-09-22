package com.sapient.notify.client.auth;

import com.sapient.notify.security.UserMasterImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
    name = "auth",
    url = "${authServiceURL}/auth"
)
public interface AuthClient {

    @GetMapping("/valid-token")
    public UserMasterImpl validateAccessToken(@RequestHeader("Authorization") String bearerToken);

    @PostMapping("/get-emails")
    public ResponseEntity<List<String>> getEmails(@RequestBody List<Long> userIdList);
}
