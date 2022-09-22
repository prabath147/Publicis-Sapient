package com.pharmacy.authservice.controller;

import com.pharmacy.authservice.client.notify.EmailClient;
import com.pharmacy.authservice.client.order.CustomerClient;
import com.pharmacy.authservice.client.pharmacy.ManagerClient;
import com.pharmacy.authservice.exception.ErrorMessage;
import com.pharmacy.authservice.exception.ResourceException;
import com.pharmacy.authservice.exception.TokenRefreshException;
import com.pharmacy.authservice.model.*;
import com.pharmacy.authservice.payload.request.*;
import com.pharmacy.authservice.payload.response.JwtResponse;
import com.pharmacy.authservice.payload.response.MessageResponse;
import com.pharmacy.authservice.payload.response.TokenRefreshResponse;
import com.pharmacy.authservice.repository.ForgetPasswordRepository;
import com.pharmacy.authservice.repository.RefreshTokenRepository;
import com.pharmacy.authservice.repository.RoleRepository;
import com.pharmacy.authservice.repository.UserMasterRepository;
import com.pharmacy.authservice.security.jwt.JwtUtils;
import com.pharmacy.authservice.service.ForgetPasswordService;
import com.pharmacy.authservice.service.RefreshTokenService;
import com.pharmacy.authservice.service.UserMasterImpl;
import com.pharmacy.authservice.service.UserMasterServiceImpl;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    @Value("${com.pharmacy.authservice.jwtSecret}")
    private String jwtSecret;

    @Value("${forgetPwdLink}")
    String pwdResetLink;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserMasterRepository userMasterRepository;

    @Autowired
    UserMasterServiceImpl userDetailsService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    ForgetPasswordRepository forgetPasswordRepository;

    @Autowired
    ForgetPasswordService forgetPasswordService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    CustomerClient customerClient;

    @Autowired
    ManagerClient managerClient;

    @Autowired
    EmailClient emailClient;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserMasterImpl userDetails = (UserMasterImpl) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            UserMaster userMaster = userMasterRepository.findByUsername(loginRequest.getUsername());
            userMaster.setLastValidTokenTime(Date.from(Instant.now()));
            userMasterRepository.save(userMaster);

            String jwt = jwtUtils.generateJwtToken(authentication);
            refreshTokenRepository.deleteAllByUser_Id(userMaster.getId());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Enter correct credentials!!");
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (Boolean.TRUE.equals(userMasterRepository.existsByUsername(signUpRequest.getUsername()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (Boolean.TRUE.equals(userMasterRepository.existsByEmail(signUpRequest.getEmail()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        UserMaster user = new UserMaster(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
        user.setLastValidTokenTime(Date.from(Instant.now()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles.isEmpty()) {
            throw new ResourceException("Role", "role not found", new ArrayList<>(), ResourceException.ErrorType.FOUND);
        }

        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
                    if (adminRole == null) {
                        throw new ResourceException("Role", "role", ERole.ROLE_ADMIN, ResourceException.ErrorType.FOUND);
                    }
                    roles.add(adminRole);
                    break;

                case "customer":
                    Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER);
                    if (userRole == null) {
                        throw new ResourceException("Role", "role", ERole.ROLE_CUSTOMER, ResourceException.ErrorType.FOUND);
                    }
                    roles.add(userRole);
                    break;

                case "manager":
                    Role preManagerRole = roleRepository.findByName(ERole.ROLE_PRE_MANAGER);
                    if (preManagerRole == null) {
                        throw new ResourceException("Role", "role", ERole.ROLE_PRE_MANAGER, ResourceException.ErrorType.FOUND);
                    }
                    roles.add(preManagerRole);
                    break;

                default:
                    throw new ErrorMessage(404, "Error: Role is not valid.");
            }
        });

        user.setRoles(roles);
        try {
            userMasterRepository.save(user);
        } catch (Exception e) {
            throw new ResourceException("userMaster", "user", user, ResourceException.ErrorType.CREATED, e);
        }

        if (signUpRequest.getRole().contains("admin")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("User registered successfully!"));
        }

        UserMaster userMaster = userMasterRepository.findByUsername(user.getUsername());
        Object detail = signUpRequest.getDetailObject();

        LoginRequest loginRequest = new LoginRequest(signUpRequest.getUsername(), signUpRequest.getPassword());
        JwtResponse response = authenticateUser(loginRequest).getBody();
        assert response != null;

        ResponseEntity<Object> details = null;
        if (signUpRequest.getRole().contains("customer")) {
            details = customerClient.createUserDetails(response.getType() + " " + response.getToken(), userMaster.getId(), detail);
        }
        else if (signUpRequest.getRole().contains("manager")) {
            details = managerClient.saveManager(response.getType() + " " + response.getToken(), userMaster.getId(), detail);
        }
        if (Objects.requireNonNull(details).getStatusCode() != HttpStatus.CREATED) {
            refreshTokenRepository.deleteAllByUser_Id(userMaster.getId());
            userMasterRepository.deleteById(userMaster.getId());
            throw new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "User not registered");
        }
        else{
            refreshTokenRepository.deleteAllByUser_Id(userMaster.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("User registered successfully!"));
        }

    }


    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> getRefreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration).map(RefreshToken::getUser).map(user -> {
            UserMaster userMaster = userMasterRepository.findByUsername(user.getUsername());
            userMaster.setLastValidTokenTime(new Date());
            userMasterRepository.save(userMaster);
            String token = jwtUtils.generateTokenFromUsername(user.getUsername());
            return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        }).orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }


    @PostMapping("/signout")
    public void logout(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        String jwt = "";

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            jwt = headerAuth.substring(7);
        }

        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        UserMaster userMaster = userMasterRepository.findByUsername(username);
        userMaster.setLastValidTokenTime(Date.from(Instant.now()));
        userMasterRepository.save(userMaster);
        refreshTokenRepository.deleteAllByUser_Id(userMaster.getId());
    }


    @GetMapping("/valid-token")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER') or hasRole('ADMIN') or hasRole('PRE_MANAGER')")
    public ResponseEntity<UserDetails> validateAccessToken(HttpServletRequest request) {

        String headerAuth = request.getHeader("Authorization");
        String jwt = "";

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            jwt = headerAuth.substring(7);
        }

        try {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UserMaster userMaster = userMasterRepository.findByUsername(username);
            Date issuedTime = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getIssuedAt();
            if ((issuedTime.getTime()) < (userMaster.getLastValidTokenTime().getTime() - 1000)) {
                throw new RuntimeException("Token is Expired");
            }

            return ResponseEntity.ok().body(userDetails);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/get-emails")
    public ResponseEntity<List<String>> getEmails(@RequestBody List<Long> userIdList) {
        List<String> emailList = new ArrayList<>();
        for (Long userId : userIdList) {
            Optional<UserMaster> userMaster = userMasterRepository.findById(userId);
            if (userMaster.isPresent()) emailList.add(userMaster.get().getEmail());
            else emailList.add("NA");
        }
        return ResponseEntity.ok().body(emailList);
    }


    @PostMapping("/reset-password/{id}")
    public ResponseEntity<String> resetPassword(@PathVariable("id") Long userId, @Valid @RequestBody ResetPassword resetPassword) {

        Optional<UserMaster> optionalUserMaster = userMasterRepository.findById(userId);
        if (optionalUserMaster.isEmpty())
            throw new ResourceException("UserMaster", "ID", userId, ResourceException.ErrorType.FOUND);
        UserMaster userMaster = optionalUserMaster.get();
        if (encoder.matches(resetPassword.getOldPassword(), userMaster.getPassword())) {
            String newPassword = encoder.encode(resetPassword.getNewPassword());
            userMaster.setPassword(newPassword);
            userMaster.setLastValidTokenTime(Date.from(Instant.now()));
            userMasterRepository.save(userMaster);
            return ResponseEntity.status(HttpStatus.OK).body("Password reset successful!!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong password");
        }
    }


    @DeleteMapping("/delete-account/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") Long userId) {
        try {
            Optional<UserMaster> userMaster = userMasterRepository.findById(userId);
            if (userMaster.isEmpty()) {
                throw new ResourceException("userMaster", "userId", userId, ResourceException.ErrorType.CREATED);
            }
            UserMaster user = userMaster.get();
            Set<Role> roles = user.getRoles();
            if (roles.toString().contains("ROLE_MANAGER")) {
                managerClient.deleteManager(userId);
            } else if (roles.toString().contains("ROLE_CUSTOMER")) {
                customerClient.deleteUserDetails(userId);
            }
            userMasterRepository.deleteById(userId);

            return ResponseEntity.status(HttpStatus.OK).body("Account deleted successfully.");
        } catch (Exception e) {
            throw new ResourceException("userMaster", "userId", userId, ResourceException.ErrorType.DELETED, e);
        }
    }

    @PostMapping("/forget-password-email")
    public ResponseEntity<String> getForgetPasswordEmail(@RequestParam String email) throws UnsupportedEncodingException {
        UserMaster userMaster = new UserMaster();
        if (userMasterRepository.existsByEmail(email))
            userMaster = userMasterRepository.findByEmail(email);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email does not exists!!");

        ForgetPassword forgetPassword = forgetPasswordService.createForgetPasswordToken(userMaster);
        String code = URLEncoder.encode(forgetPassword.getCode(),StandardCharsets.UTF_8.toString());
        List<EmailDto> emailDtoList = new ArrayList<>();
        EmailDto emailDto = new EmailDto();
        emailDto.setToEmail(email);
        emailDto.setEmailSubject("Forget Password Link");
        emailDto.setUserId(userMaster.getId());
        emailDto.setEmailBody("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title></title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "<link href=\"https://fonts.googleapis.com/css2?family=Poppins:wght@800&display=swap\" rel=\"stylesheet\">\n" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "<style type=\"text/css\">\n" +
                "    \n" +
                "\n" +
                "    body, table, td, a { -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }\n" +
                "    table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }\n" +
                "    img { -ms-interpolation-mode: bicubic; }\n" +
                "\n" +
                "    img { border: 0; height: auto; line-height: 100%; outline: none; text-decoration: none; }\n" +
                "    table { border-collapse: collapse !important; }\n" +
                "    body { height: 100% !important; margin: 0 !important; padding: 0 !important; width: 100% !important; }\n" +
                "\n" +
                "\n" +
                "    a[x-apple-data-detectors] {\n" +
                "        color: inherit !important;\n" +
                "        text-decoration: none !important;\n" +
                "        font-size: inherit !important;\n" +
                "        font-family: inherit !important;\n" +
                "        font-weight: inherit !important;\n" +
                "        line-height: inherit !important;\n" +
                "    }\n" +
                "\n" +
                "    @media screen and (max-width: 480px) {\n" +
                "        .mobile-hide {\n" +
                "            display: none !important;\n" +
                "        }\n" +
                "        .mobile-center {\n" +
                "            text-align: center !important;\n" +
                "        }\n" +
                "    }\n" +
                "    div[style*=\"margin: 16px 0;\"] { margin: 0 !important; }\n" +
                "</style>\n" +
                "</head>\n" +
                "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\n" +
                "\n" +
                "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "        <tr>\n" +
                "            <td align=\"center\" style=\"background-color: #eeeeee;\" bgcolor=\"#eeeeee\">\n" +
                "            \n" +
                "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px;\">\n" +
                "                <tr>\n" +
                "                    <td align=\"center\" valign=\"top\" style=\"font-size:0; padding: 35px;\" bgcolor=\"#dae5f7\">\n" +
                "                \n" +
                "                        <div style=\"display:inline-block; vertical-align:top; width:100%;\">\n" +
                "                            <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:300px;\">\n" +
                "                                <tr>\n" +
                "                                    <a href=\"https://d1j9yqk9s11go2.cloudfront.net/\"><img src=\"https://i.ibb.co/K9Jbw7H/logo.png\" alt=\"\"  width=\"100%\" height=\"350\" ></a>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </div>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td align=\"center\" style=\"padding: 5px 35px 5px 35px; background-color: #ffffff; bgcolor:#ffffff\" >\n" +
                "                        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width:600px; background: #ffffff;\">\n" +
                "                            <tr>\n" +
                "                                <td align=\"center\" style=\"font-family: 'Poppins', sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding-top: 25px;\">\n" +
                "                                    <img src=\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQioGPEPf03gOVSsnPXI_RAXIYur_qj0GtrVw&usqp=CAU\" width=\"125\" height=\"120\" style=\"display: block; border: 0px;\" /><br>\n" +
                "                                    <h2 style=\"font-size: 30px; font-weight: 800; line-height: 36px; color: #000; margin: 0;\">\n" +
                "                                        Please reset your password\n" +
                "                                    </h2>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td align=\"left\" >\n" +
                "                                    <h3>Hello "+ userMaster.getUsername() + ",</h3>\n" +
                "                                    <p style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 200; line-height: 24px; color: #524e4e; \">\n" +
                "                                        We have sent you this email in response to your request to reset your password on company name. To reset your password, please follow the link below:\n" +
                "                                    </p>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 18px 12px 18px; border-radius:5px; background-color: #1F7F4C;\" align=\"center\">\n" +
                "                                        <a rel=\"noopener\" target=\"_blank\" href=" + pwdResetLink + code + " target=\"_blank\" style=\"font-size: 18px; font-family: Helvetica, Arial, sans-serif; font-weight: bold; color: #ffffff; text-decoration: none; display: inline-block;\">Reset Password</a>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                \n" +
                "                            </table>\n" +
                "                            <table>\n" +
                "                                <td align=\"left\" >\n" +
                "                                    <p style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 200; line-height: 24px; color: #524e4e; \">\n" +
                "                                        Please ignore this email if you did not request a password change.\n" +
                "                                    </p>\n" +
                "                                </td>\n" +
                "                            </table>\n" +
                "                            <table align=\"left\">\n" +
                "                                <tr>\n" +
                "                                    <td>\n" +
                "                                        <h3 style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px;\">\n" +
                "                                            Team PillZone\n" +
                "                                        </h3>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "    \n" +
                "</body>\n" +
                "</html>");
        emailDtoList.add(emailDto);

        LoginRequest loginRequest = new LoginRequest("admin0", "admin0");
        JwtResponse response = authenticateUser(loginRequest).getBody();
        assert(response!=null);
        emailClient.sendEmail(response.getType() + " " + response.getToken(), emailDtoList);

        return ResponseEntity.status(HttpStatus.OK).body(forgetPassword.getCode());
    }

    @GetMapping("/valid-forget-password-token")
    public ResponseEntity<String> checkForgetPasswordToken(@RequestParam String code) throws UnsupportedEncodingException {
        code = URLDecoder.decode(code, StandardCharsets.UTF_8.toString());
        if (!forgetPasswordService.doesCodeExist(code)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wrong Token!!");
        } else if (forgetPasswordService.isExpired(code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is Expired!!");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Token is Valid!!");
        }
    }

    @PostMapping("/forget-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String code, @Valid @RequestBody ForgetPasswordRequest request) throws UnsupportedEncodingException {
        code = URLDecoder.decode(code, StandardCharsets.UTF_8.toString());
        if (!forgetPasswordService.doesCodeExist(code)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wrong Token!!");
        } else if (forgetPasswordService.isExpired(code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is Expired!!");
        } else {
            ForgetPassword forgetPassword = forgetPasswordRepository.findByCode(code);
            String username = forgetPasswordService.findUsernameFromToken(forgetPassword.getToken());
            UserMaster userMaster = userMasterRepository.findByUsername(username);
            userMaster.setPassword(encoder.encode(request.getNewPassword()));
            userMaster.setLastValidTokenTime(Date.from(Instant.now()));
            userMasterRepository.save(userMaster);
            forgetPasswordRepository.deleteByCode(code);
            return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully!!");
        }
    }
}
