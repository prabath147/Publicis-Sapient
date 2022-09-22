package com.pharmacy.service.utils;

import com.pharmacy.service.client.auth.AuthClient;
import com.pharmacy.service.security.UserMasterImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthClient authClient;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = jwtUtils.parseJwt(request);
            if (jwt != null) {

                UserMasterImpl userMaster = authClient.validateAccessToken("Bearer " + jwt);

                assert userMaster != null;
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userMaster, jwt, userMaster.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: " + e);
        }

        filterChain.doFilter(request, response);
    }
}
