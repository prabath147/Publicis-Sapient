package com.pharmacy.authservice.security.jwt;

import com.pharmacy.authservice.model.UserMaster;
import com.pharmacy.authservice.repository.UserMasterRepository;
import com.pharmacy.authservice.service.UserMasterServiceImpl;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    @Value("${com.pharmacy.authservice.jwtSecret}")
    private String jwtSecret;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserMasterServiceImpl userDetailsService;

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = jwtUtils.parseJwt(request);
            if (jwt != null) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserMaster userMaster = userMasterRepository.findByUsername(username);
                Date issuedTime = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getIssuedAt();
                if ((issuedTime.getTime()) < (userMaster.getLastValidTokenTime().getTime() - 1000)) {
                    throw new RuntimeException("Token is Expired");
                }
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, jwt, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.info("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }
}
