package com.pharmacy.authservice.service;

import com.pharmacy.authservice.model.ForgetPassword;
import com.pharmacy.authservice.model.UserMaster;
import com.pharmacy.authservice.repository.ForgetPasswordRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Service
public class ForgetPasswordService {
    @Autowired
    ForgetPasswordRepository forgetPasswordRepository;

    @Autowired
    PasswordEncoder encoder;

    @Value("${com.pharmacy.authservice.pwdTokenexpirationMs}")
    private Long pwdTokenDurationMs;

    @Value("${com.pharmacy.authservice.forgetPwdSecret}")
    private String forgetPasswordSecret;

    public ForgetPassword createForgetPasswordToken(UserMaster userMaster) {
        ForgetPassword forgetPassword = new ForgetPassword();
        forgetPassword.setToken(Jwts.builder().setSubject((userMaster.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + pwdTokenDurationMs))
                .signWith(SignatureAlgorithm.HS512, forgetPasswordSecret)
                .claim("id", userMaster.getId())
                .compact());
        forgetPassword.setCode(encoder.encode(forgetPassword.getToken()));
        forgetPasswordRepository.save(forgetPassword);
        return forgetPassword;
    }

    public String findUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(forgetPasswordSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean doesCodeExist(String code) {
        return forgetPasswordRepository.existsByCode(code);
    }

    public boolean isExpired(String code) {
        ForgetPassword forgetPassword = forgetPasswordRepository.findByCode(code);
        if (Jwts.parser().setSigningKey(forgetPasswordSecret).parseClaimsJws(forgetPassword.getToken()).getBody().getExpiration().compareTo(new Date()) < 0) {
            forgetPasswordRepository.deleteByCode(code);
            return true;
        }
        else return false;
    }
}
