package com.pharmacy.authservice.controller;

import com.pharmacy.authservice.exception.ResourceException;
import com.pharmacy.authservice.model.ERole;
import com.pharmacy.authservice.payload.request.SignupRequest;
import com.pharmacy.authservice.repository.RoleRepository;
import com.pharmacy.authservice.repository.UserMasterRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@RunWith(Parameterized.class)
@Slf4j
class MockAuthControllerTest {
    @InjectMocks
    AuthController authController;

    @Mock
    UserMasterRepository userMasterRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder encoder;

    String role;

    @Test
    void mockRegisterUserWithAdminRole() {
        when(userMasterRepository.existsByUsername((String) any())).thenReturn(false);
        when(userMasterRepository.existsByEmail((String) any())).thenReturn(false);
        when(roleRepository.findByName((ERole) any())).thenReturn(null);

        Set<String> roles = new HashSet<>();
        roles.add("admin");
        SignupRequest signupRequest = new SignupRequest("mockUser3", "new@gmail.com", roles,
                "#123Mock", new Object());
        assertThrows(ResourceException.class, () -> {
            authController.registerUser(signupRequest);
        });
    }

    @Test
    void mockRegisterUserWithManagerRole() {
        when(userMasterRepository.existsByUsername((String) any())).thenReturn(false);
        when(userMasterRepository.existsByEmail((String) any())).thenReturn(false);
        when(roleRepository.findByName((ERole) any())).thenReturn(null);

        Set<String> roles = new HashSet<>();
        roles.add("manager");
        SignupRequest signupRequest = new SignupRequest("mockUser3", "new@gmail.com", roles,
                "#123Mock", new Object());
        assertThrows(ResourceException.class, () -> {
            authController.registerUser(signupRequest);
        });
    }

    @Test
    void mockRegisterUserWithCustomerRole() {
        when(userMasterRepository.existsByUsername((String) any())).thenReturn(false);
        when(userMasterRepository.existsByEmail((String) any())).thenReturn(false);
        when(roleRepository.findByName((ERole) any())).thenReturn(null);

        Set<String> roles = new HashSet<>();
        roles.add("customer");
        SignupRequest signupRequest = new SignupRequest("mockUser3", "new@gmail.com", roles,
                "#123Mock", new Object());
        assertThrows(ResourceException.class, () -> {
            authController.registerUser(signupRequest);
        });
    }
}
