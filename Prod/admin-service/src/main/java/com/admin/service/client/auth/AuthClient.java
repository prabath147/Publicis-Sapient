package com.admin.service.client.auth;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.admin.service.security.UserMasterImpl;

@FeignClient(
    name = "auth",
    url = "${authServiceURL}/auth"
)
public interface AuthClient {

    @GetMapping("/valid-token")
    public UserMasterImpl validateAccessToken(@RequestHeader("Authorization") String bearerToken);

}
