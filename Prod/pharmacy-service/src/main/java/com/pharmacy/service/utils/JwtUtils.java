package com.pharmacy.service.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class JwtUtils {

    private static String jwtSecretStat;
    @Value("${com.pharmacy.authservice.jwtSecret}")
    private String jwtSecret;

    public static boolean verifyId(String jwt, Long id, boolean isUpdate) {
        try {
            jwt = jwt.substring(7);
            return isUpdate ? Jwts.parser().setSigningKey("pharmacySecretKey").parseClaimsJws(jwt).getBody().get("id").toString().equals(id.toString()) : Jwts.parser().setSigningKey("pharmacySecretKey").parseClaimsJws(jwt).getBody().get("id").toString().equals(id.toString()) || Jwts.parser().setSigningKey("pharmacySecretKey").parseClaimsJws(jwt).getBody().get("role").toString().equals("ROLE_ADMIN");
        }catch (Exception e){
            return false;
        }
    }

    @Value("${com.pharmacy.authservice.jwtSecret}")
    public static void setJwtSecretStat(String secretStat) {
        jwtSecretStat = secretStat;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

}