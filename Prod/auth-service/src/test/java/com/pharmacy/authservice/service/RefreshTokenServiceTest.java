package com.pharmacy.authservice.service;

import com.pharmacy.authservice.exception.ResourceException;
import com.pharmacy.authservice.exception.TokenRefreshException;
import com.pharmacy.authservice.model.ERole;
import com.pharmacy.authservice.model.RefreshToken;
import com.pharmacy.authservice.model.Role;
import com.pharmacy.authservice.model.UserMaster;
import com.pharmacy.authservice.repository.RefreshTokenRepository;
import com.pharmacy.authservice.repository.RoleRepository;
import com.pharmacy.authservice.repository.UserMasterRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@WebAppConfiguration
class RefreshTokenServiceTest {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_MANAGER));
        UserMaster userMaster = new UserMaster("mockUser3","mock3@gmail.com",encoder.encode("mockPwd"),roles);
        userMasterRepository.save(userMaster);
    }

    @AfterEach
    void tearDown() {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");
        refreshTokenService.deleteByUserId(userMaster.getId());
        userMasterRepository.deleteByUsername("mockUser3");
    }

    @Test
    void findByToken() {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userMaster);
        refreshToken.setExpiryDate(Instant.now().plusMillis(86400000));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);

        assertNotNull(refreshTokenService.findByToken(refreshToken.getToken()));
    }

    @Test
    void createRefreshToken() {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userMaster.getId());
        assertEquals(userMaster.getUsername(),refreshToken.getUser().getUsername());
    }

    @Test
    void createRefreshToken2() {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_MANAGER));
        UserMaster userMaster = new UserMaster("mockUser4","mock4@gmail.com",encoder.encode("mockPwd"),roles);
        userMasterRepository.save(userMaster);
        UserMaster userMaster1 = userMasterRepository.findByUsername("mockUser4");
        userMasterRepository.deleteById(userMaster1.getId());
        assertThrows(ResourceException.class, () -> {
            refreshTokenService.createRefreshToken(userMaster1.getId());
        });
    }

    @Test
    void verifyExpirationWhichDoesNotThrowException() throws InterruptedException {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userMaster.getId());
        refreshToken.setExpiryDate(Instant.now().plusMillis(1));

        RefreshToken token = refreshTokenService.verifyExpiration(refreshToken);
        assertEquals(refreshToken,token);
    }

    @Test
    void verifyExpirationWhichThrowsException() throws InterruptedException {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userMaster.getId());
        refreshToken.setExpiryDate(Instant.now().plusMillis(1));
        TimeUnit.MILLISECONDS.sleep(1);

        assertThrows(TokenRefreshException.class, () -> {
            refreshTokenService.verifyExpiration(refreshToken);
        });
    }

    @Test
    void deleteByUserId() {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userMaster.getId());
        refreshTokenRepository.save(refreshToken);

        refreshTokenService.deleteByUserId(userMaster.getId());
        Optional<RefreshToken> actual = refreshTokenRepository.findByToken(refreshToken.getToken());
        if (actual.isEmpty())
            actual = null;
        assertNull(actual);
    }

}