package com.pharmacy.authservice.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.pharmacy.authservice.model.ERole;
import com.pharmacy.authservice.model.ForgetPassword;
import com.pharmacy.authservice.model.RefreshToken;
import com.pharmacy.authservice.model.Role;
import com.pharmacy.authservice.model.UserMaster;
import com.pharmacy.authservice.payload.request.ForgetPasswordRequest;
import com.pharmacy.authservice.payload.request.LoginRequest;
import com.pharmacy.authservice.payload.request.ResetPassword;
import com.pharmacy.authservice.payload.request.SignupRequest;
import com.pharmacy.authservice.payload.request.TokenRefreshRequest;
import com.pharmacy.authservice.payload.response.JwtResponse;
import com.pharmacy.authservice.repository.ForgetPasswordRepository;
import com.pharmacy.authservice.repository.RefreshTokenRepository;
import com.pharmacy.authservice.repository.RoleRepository;
import com.pharmacy.authservice.repository.UserMasterRepository;
import com.pharmacy.authservice.service.ForgetPasswordService;
import com.pharmacy.authservice.service.RefreshTokenService;
import com.pharmacy.authservice.utils.CustomMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
class AuthControllerTest {

    @Value("${com.pharmacy.authservice.forgetPwdSecret}")
    private String forgetPasswordSecret;

    private static WireMockServer wireMockServer;
    protected MockMvc mvc;
    protected MvcResult mvcResult;
    protected String uri;
    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    ForgetPasswordRepository forgetPasswordRepository;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    ForgetPasswordService forgetPasswordService;

    @Autowired
    PasswordEncoder encoder;

    protected CustomMapper customMapper = new CustomMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_ADMIN));
        UserMaster userMaster = new UserMaster("mockUser3", "mock3@gmail.com", encoder.encode("mockPwd"), roles);
        userMasterRepository.save(userMaster);
    }

    @AfterEach
    void tearDown() {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");
        refreshTokenService.deleteByUserId(userMaster.getId());
        userMasterRepository.deleteByUsername("mockUser3");
        userMasterRepository.deleteByUsername("mockUser4");
    }

    @Test
    void registerUserWithExistingUserName() throws Exception {
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        SignupRequest signupRequest = new SignupRequest("mockUser3", "new@gmail.com", roles,
                "#123Mock", new Object());

        uri = "/api/auth/signup";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(signupRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
    }

    @Test
    void registerUserWithExistingEmail() throws Exception {
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        SignupRequest signupRequest = new SignupRequest("mockUser4", "mock3@gmail.com", roles,
                "#123Mock", new Object());

        uri = "/api/auth/signup";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(signupRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
    }

    @Test
    void registerUserWithAdminRole() throws Exception {
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        SignupRequest signupRequest = new SignupRequest("mockUser4", "mock4@gmail.com", roles,
                "#123Mock", new Object());
        uri = "/api/auth/signup";

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(signupRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
        int status = mvcResult.getResponse().getStatus();
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser4");
        refreshTokenRepository.deleteAllByUser_Id(userMaster.getId());
        userMasterRepository.deleteByUsername("mockUser4");
        assertEquals(201, status);
    }

    @Test
    void registerUserWithManagerRole() throws Exception {
        wireMockServer = new WireMockServer(
                new WireMockConfiguration().port(8082)
        );
        wireMockServer.start();
        WireMock.configureFor("localhost", 8082);

        Set<String> roles = new HashSet<>();
        roles.add("manager");
        SignupRequest signupRequest = new SignupRequest("mockUser4", "mock4@gmail.com", roles,
                "#123Mock", new Object());
        uri = "/api/auth/signup";

        stubFor(WireMock.post(urlMatching("/pharmacy/manager/create-manager/([0-9]+)"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.CREATED.value())));

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(signupRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        userMasterRepository.deleteByUsername("mockUser4");
        wireMockServer.stop();
    }

    @Test
    void registerUserWithCustomerRole() throws Exception {
        wireMockServer = new WireMockServer(
                new WireMockConfiguration().port(8083)
        );
        wireMockServer.start();
        WireMock.configureFor("localhost", 8083);

        Set<String> roles = new HashSet<>();
        roles.add("customer");
        SignupRequest signupRequest = new SignupRequest("mockUser4", "mock4@gmail.com", roles,
                "#123Mock", new Object());
        uri = "/api/auth/signup";

        stubFor(WireMock.post(urlMatching("/order/user-details/create-user-details/([0-9]+)"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.CREATED.value())));

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(signupRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        userMasterRepository.deleteByUsername("mockUser4");
        wireMockServer.stop();
    }

    @Test
    void registerUserWhenOtherServiceFails() throws Exception {
        wireMockServer = new WireMockServer(
                new WireMockConfiguration().port(8083)
        );
        wireMockServer.start();
        WireMock.configureFor("localhost", 8083);

        Set<String> roles = new HashSet<>();
        roles.add("customer");
        SignupRequest signupRequest = new SignupRequest("mockUser4", "mock4@gmail.com", roles,
                "#123Mock", new Object());
        uri = "/api/auth/signup";

        stubFor(WireMock.post(urlMatching("/order/user-details/create-user-details/([0-9]+)"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())));

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(signupRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
        userMasterRepository.deleteByUsername("mockUser4");
        wireMockServer.stop();
    }

    @Test
    void registerUserWithInvalidRole() throws Exception {
        Set<String> roles = new HashSet<>();
        roles.add("role");
        SignupRequest signupRequest = new SignupRequest("mockUser4", "mock4@gmail.com", roles,
                "#123Mock", new Object());
        uri = "/api/auth/signup";

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(signupRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

    @Test
    void registerUserWithoutRole() throws Exception {
        Set<String> roles = new HashSet<>();
        SignupRequest signupRequest = new SignupRequest("mockUser4", "mock4@gmail.com", roles,
                "#123Mock", new Object());
        uri = "/api/auth/signup";

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(signupRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

    @Test
    void authenticateUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest("mockUser3", "mockPwd");
        uri = "/api/auth/signin";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(loginRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void authenticateUserWithWrongUserName() throws Exception {
        LoginRequest loginRequest = new LoginRequest("mockUser2", "pwd");
        uri = "/api/auth/signin";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(loginRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(401, status);
    }

    @Test
    void authenticateUserWithWrongPassword() throws Exception {
        LoginRequest loginRequest = new LoginRequest("mockUser3", "pwd");
        uri = "/api/auth/signin";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(loginRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(401, status);
    }

    @Test
    void getRefreshToken() throws Exception {
        UserMaster user = userMasterRepository.findByUsername("mockUser3");
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
        tokenRefreshRequest.setRefreshToken(refreshToken.getToken());
        uri = "/api/auth/refresh-token";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(tokenRefreshRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void getRefreshTokenWithWrongToken() throws Exception {
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
        tokenRefreshRequest.setRefreshToken("2d3d376c-42e1-4c1c-9c5e-99bd567f1124");
        uri = "/api/auth/refresh-token";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(tokenRefreshRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

    @Test
    void validateAccessToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("mockUser3", "mockPwd");
        uri = "/api/auth/signin";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(loginRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JwtResponse response = customMapper.mapFromJson(result, JwtResponse.class);

        uri = "/api/auth/valid-token";
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", "Bearer " + response.getToken())
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void validateAccessTokenWithWrongToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("mockUser3", "mockPwd");
        uri = "/api/auth/signin";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(loginRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JwtResponse response = customMapper.mapFromJson(result, JwtResponse.class);

        uri = "/api/auth/valid-token";
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Authorization", response.getToken())
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

    @Test
    void logout() throws Exception {
        LoginRequest loginRequest = new LoginRequest("mockUser3", "mockPwd");
        uri = "/api/auth/signin";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(loginRequest))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        JwtResponse response = customMapper.mapFromJson(result, JwtResponse.class);

        uri = "/api/auth/signout";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .header("Authorization", "Bearer " + response.getToken())
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        Optional<RefreshToken> token = refreshTokenRepository.findByToken(response.getRefreshToken());
        if (token.isEmpty())
            token = null;

        assertNull(token);
    }

    @Test
    void getEmails() throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_ADMIN));
        List<UserMaster> users = new ArrayList<>();
        UserMaster userMaster = new UserMaster(100L, "mockUser5", "mock5@gmail.com", encoder.encode("mockPwd"), roles);
        UserMaster userMaster1 = new UserMaster(200L, "mockUser6", "mock6@gmail.com", encoder.encode("mockPwd"), roles);
        UserMaster userMaster2 = new UserMaster(300L, "mockUser7", "mock7@gmail.com", encoder.encode("mockPwd"), roles);
        UserMaster userMaster3 = new UserMaster(400L, "mockUser8", "mock8@gmail.com", encoder.encode("mockPwd"), roles);
        Collections.addAll(users, userMaster, userMaster1, userMaster2, userMaster3);
        userMasterRepository.saveAll(users);

        Long id1 = userMasterRepository.findByUsername("mockUser5").getId();
        Long id2 = userMasterRepository.findByUsername("mockUser6").getId();
        Long id3 = userMasterRepository.findByUsername("mockUser7").getId();
        Long id4 = userMasterRepository.findByUsername("mockUser8").getId();
        List<Long> userIdList = new ArrayList<>();
        Collections.addAll(userIdList, id1, id2, id3, id4);

        uri = "/api/auth/get-emails";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(String.valueOf(userIdList))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        List emails = customMapper.mapFromJson(result, List.class);

        assertEquals("mock5@gmail.com", emails.get(0));
        assertEquals("mock6@gmail.com", emails.get(1));
        assertEquals("mock7@gmail.com", emails.get(2));
        assertEquals("mock8@gmail.com", emails.get(3));

        userMasterRepository.deleteAllByIdInBatch(userIdList);
    }

    @Test
    void getEmails2() throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_ADMIN));
        List<UserMaster> users = new ArrayList<>();
        UserMaster userMaster = new UserMaster(100L, "mockUser5", "mock5@gmail.com", encoder.encode("mockPwd"), roles);
        UserMaster userMaster1 = new UserMaster(200L, "mockUser6", "mock6@gmail.com", encoder.encode("mockPwd"), roles);
        UserMaster userMaster2 = new UserMaster(300L, "mockUser7", "mock7@gmail.com", encoder.encode("mockPwd"), roles);
        UserMaster userMaster3 = new UserMaster(400L, "mockUser8", "mock8@gmail.com", encoder.encode("mockPwd"), roles);
        Collections.addAll(users, userMaster, userMaster1, userMaster2, userMaster3);
        userMasterRepository.saveAll(users);

        Long id1 = userMasterRepository.findByUsername("mockUser5").getId();
        Long id2 = userMasterRepository.findByUsername("mockUser6").getId();
        Long id3 = userMasterRepository.findByUsername("mockUser7").getId();
        Long id4 = userMasterRepository.findByUsername("mockUser8").getId();
        List<Long> userIdList = new ArrayList<>();
        Collections.addAll(userIdList, id1, id2, id3, id4);
        userMasterRepository.deleteByUsername("mockUser6");

        uri = "/api/auth/get-emails";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(String.valueOf(userIdList))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        List emails = customMapper.mapFromJson(result, List.class);

        assertEquals("mock5@gmail.com", emails.get(0));
        assertEquals("NA", emails.get(1));
        assertEquals("mock7@gmail.com", emails.get(2));
        assertEquals("mock8@gmail.com", emails.get(3));

        userMasterRepository.deleteAllByIdInBatch(userIdList);
    }

    @Test
    void resetPassword() throws Exception {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");
        ResetPassword resetPassword = new ResetPassword("mockPwd", "#123Mock");
        uri = "/api/auth/reset-password/" + userMaster.getId();
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(resetPassword))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void resetPasswordWithWrongPassword() throws Exception {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");
        ResetPassword resetPassword = new ResetPassword("pwd", "#123Mock");
        uri = "/api/auth/reset-password/" + userMaster.getId();
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(resetPassword))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400,status);
    }

    @Test
    void resetPasswordForNonExistingId() throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_MANAGER));
        UserMaster userMaster = new UserMaster("mockUser4","mock4@gmail.com",encoder.encode("mockPwd"),roles);
        userMasterRepository.save(userMaster);
        UserMaster userMaster1 = userMasterRepository.findByUsername("mockUser4");
        userMasterRepository.deleteById(userMaster1.getId());

        ResetPassword resetPassword = new ResetPassword("pwd", "#123Mock");
        uri = "/api/auth/reset-password/" + userMaster1.getId();
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).content(customMapper.mapToJson(resetPassword))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

    @Test
    void deleteManager() throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_MANAGER));
        UserMaster user = new UserMaster("mockUser4", "mock4@gmail.com", encoder.encode("mockPwd"), roles);
        userMasterRepository.save(user);
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser4");

        wireMockServer = new WireMockServer(
                new WireMockConfiguration().port(8082)
        );
        wireMockServer.start();
        WireMock.configureFor("localhost", 8082);

        stubFor(WireMock.delete(urlMatching("/pharmacy/manager/delete-manager/([0-9]+)"))
                .willReturn(aResponse()
                        .withStatus(200)));

        uri = "/api/auth/delete-account/" + userMaster.getId();
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        wireMockServer.stop();
    }

    @Test
    void deleteCustomer() throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_CUSTOMER));
        UserMaster user = new UserMaster("mockUser4", "mock4@gmail.com", encoder.encode("mockPwd"), roles);
        userMasterRepository.save(user);
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser4");

        wireMockServer = new WireMockServer(
                new WireMockConfiguration().port(8083)
        );
        wireMockServer.start();
        WireMock.configureFor("localhost", 8083);

        stubFor(WireMock.delete(urlMatching("/order/user-details/delete-user-details/([0-9]+)"))
                .willReturn(aResponse()
                        .withStatus(200)));

        uri = "/api/auth/delete-account/" + userMaster.getId();
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        wireMockServer.stop();
    }

    @Test
    void deleteNonExistingUser() throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_CUSTOMER));
        UserMaster user = new UserMaster("mockUser4", "mock4@gmail.com", encoder.encode("mockPwd"), roles);
        userMasterRepository.save(user);
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser4");
        userMasterRepository.deleteById(userMaster.getId());
        wireMockServer = new WireMockServer(
                new WireMockConfiguration().port(8083)
        );
        wireMockServer.start();
        WireMock.configureFor("localhost", 8083);

        stubFor(WireMock.delete(urlMatching("/order/user-details/delete-user-details/([0-9]+)"))
                .willReturn(aResponse()
                        .withStatus(200)));

        uri = "/api/auth/delete-account/" + userMaster.getId();
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
        wireMockServer.stop();
    }

    @Test
    void getForgetPasswordEmail() throws Exception {
        wireMockServer = new WireMockServer(
                new WireMockConfiguration().port(8085)
        );
        wireMockServer.start();
        WireMock.configureFor("localhost", 8085);

        stubFor(WireMock.post(urlMatching("/notify/mail/send"))
                .willReturn(aResponse()
                        .withStatus(200)));

        uri = "/api/auth/forget-password-email";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("email", "mock3@gmail.com")
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        wireMockServer.stop();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }
    @Test
    void getForgetPasswordEmail2() throws Exception {
        wireMockServer = new WireMockServer(
                new WireMockConfiguration().port(8085)
        );
        wireMockServer.start();
        WireMock.configureFor("localhost", 8085);

        stubFor(WireMock.post(urlMatching("/notify/mail/send"))
                .willReturn(aResponse()
                        .withStatus(200)));

        uri = "/api/auth/forget-password-email";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("email", "random@gmail.com")
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        wireMockServer.stop();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    void checkForgetPasswordToken() throws Exception {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");
        ForgetPassword forgetPassword = forgetPasswordService.createForgetPasswordToken(userMaster);
        String code = URLEncoder.encode(forgetPassword.getCode(), StandardCharsets.UTF_8.toString());

        uri = "/api/auth/valid-forget-password-token";
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).param("code", code)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void checkForgetPasswordToken2() throws Exception {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");
        ForgetPassword forgetPassword = forgetPasswordService.createForgetPasswordToken(userMaster);
        String code = URLEncoder.encode(forgetPassword.getCode(), StandardCharsets.UTF_8.toString());
        forgetPasswordRepository.deleteByCode(forgetPassword.getCode());
        uri = "/api/auth/valid-forget-password-token";
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).param("code", code)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    void checkForgetPasswordToken3() throws Exception {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");
        ForgetPassword forgetPassword = new ForgetPassword();
        forgetPassword.setToken(Jwts.builder().setSubject((userMaster.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 1))
                .signWith(SignatureAlgorithm.HS512, forgetPasswordSecret)
                .claim("id", userMaster.getId())
                .compact());
        forgetPassword.setCode(encoder.encode(forgetPassword.getToken()));
        forgetPasswordRepository.save(forgetPassword);
        TimeUnit.MILLISECONDS.sleep(1);
        String code = URLEncoder.encode(forgetPassword.getCode(), StandardCharsets.UTF_8.toString());

        uri = "/api/auth/valid-forget-password-token";
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).param("code", code)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

    @Test
    void forgotPassword() throws Exception {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");
        ForgetPassword forgetPassword = forgetPasswordService.createForgetPasswordToken(userMaster);
        String code = URLEncoder.encode(forgetPassword.getCode(), StandardCharsets.UTF_8.toString());
        ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest("#1234Mock");

        uri = "/api/auth/forget-password/";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("code", code)
                .content(customMapper.mapToJson(forgetPasswordRequest)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void forgotPassword2() throws Exception {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");
        ForgetPassword forgetPassword = forgetPasswordService.createForgetPasswordToken(userMaster);
        String code = URLEncoder.encode(forgetPassword.getCode(), StandardCharsets.UTF_8.toString());
        ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest("#1234Mock");
        forgetPasswordRepository.deleteByCode(forgetPassword.getCode());

        uri = "/api/auth/forget-password/";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("code", code)
                .content(customMapper.mapToJson(forgetPasswordRequest)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    void forgotPassword3() throws Exception {
        UserMaster userMaster = userMasterRepository.findByUsername("mockUser3");
        ForgetPassword forgetPassword = new ForgetPassword();
        forgetPassword.setToken(Jwts.builder().setSubject((userMaster.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 1))
                .signWith(SignatureAlgorithm.HS512, forgetPasswordSecret)
                .claim("id", userMaster.getId())
                .compact());
        forgetPassword.setCode(encoder.encode(forgetPassword.getToken()));
        forgetPasswordRepository.save(forgetPassword);
        TimeUnit.MILLISECONDS.sleep(1);
        String code = URLEncoder.encode(forgetPassword.getCode(), StandardCharsets.UTF_8.toString());
        ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest("#1234Mock");
        uri = "/api/auth/forget-password/";
        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).param("code", code)
                .content(customMapper.mapToJson(forgetPasswordRequest)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

}