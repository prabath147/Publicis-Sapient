package com.pharmacy.authservice.security.jwt;

import com.pharmacy.authservice.model.Role;
import com.pharmacy.authservice.model.UserMaster;
import com.pharmacy.authservice.repository.UserMasterRepository;
import com.pharmacy.authservice.service.UserMasterImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Autowired
  UserMasterRepository userMasterRepository;

  @Value("${com.pharmacy.authservice.jwtSecret}")
  private String jwtSecret;

  @Value("${com.pharmacy.authservice.jwtExpirationMs}")
  private int jwtExpirationMs;

  public String generateJwtToken(Authentication authentication) {

    UserMasterImpl userPrincipal = (UserMasterImpl) authentication.getPrincipal();
    String role = "";
    for (GrantedAuthority authority : userPrincipal.getAuthorities()) {
      role = authority.toString();
    }

    return Jwts.builder().setSubject((userPrincipal.getUsername()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .claim("role", role).claim("id", userPrincipal.getId())
        .compact();
  }

  public String generateTokenFromUsername(String username) {
    UserMaster userMaster = userMasterRepository.findByUsername(username);
    Optional<Role> roles = userMaster.getRoles().stream().findFirst();
    String role = "";
    if (roles.isPresent()) {
      role = roles.get().getName().toString();
    }
    return Jwts.builder().setSubject(username).setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .claim("role",role).claim("id", userMaster.getId())
            .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
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
