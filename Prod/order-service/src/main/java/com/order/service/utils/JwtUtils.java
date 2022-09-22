package com.order.service.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class JwtUtils {

    @Value("${com.pharmacy.authservice.jwtSecret}")
    private String jwtSecret;

    @Value("${com.pharmacy.authservice.jwtSecret}")
    private String jwtSecretStat;

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

    public boolean verifyId(String jwt, Long id, boolean isUpdate) {
        jwt = jwt.substring(7);
        return isUpdate ? Jwts.parser().setSigningKey(jwtSecretStat)
                .parseClaimsJws(jwt).getBody().get("id").toString().equals(id.toString())
                : Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().get("id").toString()
                        .equals(id.toString())
                        || Jwts.parser().setSigningKey(jwtSecretStat)
                                .parseClaimsJws(jwt).getBody().get("role").toString().equals("ROLE_ADMIN");
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
