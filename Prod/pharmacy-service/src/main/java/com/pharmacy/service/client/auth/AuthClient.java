package com.pharmacy.service.client.auth;


import com.pharmacy.service.dto.JwtResponse;
import com.pharmacy.service.dto.LoginRequest;
import com.pharmacy.service.security.UserMasterImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;

@FeignClient(
    name = "auth",
    url = "${authServiceURL}/auth"
)
public interface AuthClient {
    @GetMapping("/valid-token")
    UserMasterImpl validateAccessToken(@RequestHeader("Authorization") String bearerToken);

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest);

    @PostMapping("/signout")
    public void logout(@RequestHeader("Authorization") String bearerToken);
}
