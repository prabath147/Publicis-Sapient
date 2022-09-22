package com.sapient.notify.configuration;

import com.sapient.notify.client.auth.AuthClient;
import com.sapient.notify.security.UserMasterImpl;
import com.sapient.notify.utils.JwtUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthClient authClient;

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/ws");
    }
    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
//        registry.addEndpoint("/notify-ws").setAllowedOriginPatterns("*");
        registry.addEndpoint("/notify-ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // configure inbound channel
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
                // configure pre-send
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,StompHeaderAccessor.class);
                assert accessor != null;
                StompCommand command = accessor.getCommand();
                assert command != null;
                if(StompCommand.CONNECT.equals(command)){
                    // handle when a client connection request comes
                    String jwt = accessor.getFirstNativeHeader("token");
                    assert jwt != null;

                    if(jwtUtils.validateJwtToken(jwt)){
                        // validate jwt token using auth client
                        UserMasterImpl userMaster = authClient.validateAccessToken("Bearer " + jwt);
                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                userMaster, null,
                                userMaster == null ?
                                        List.of() : userMaster.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        accessor.setUser(auth);
                    }
                }else if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())){
                    // handle subscribe request - set destination based on accessor user detail
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) accessor.getUser();
                    assert usernamePasswordAuthenticationToken != null;
                    UserMasterImpl userDetails = (UserMasterImpl) usernamePasswordAuthenticationToken.getPrincipal();
                    Long userId = userDetails.getId();
                    accessor.setHeader("simpDestination",accessor.getHeader("simpDestination")+String.valueOf(userId));
                    log.info("Destination "+accessor.getDestination());
                }
                return message;
            }
        });
    }
}
