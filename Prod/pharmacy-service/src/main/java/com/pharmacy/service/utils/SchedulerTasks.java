package com.pharmacy.service.utils;

import com.pharmacy.service.client.auth.AuthClient;
import com.pharmacy.service.client.notify.NotificationClient;
import com.pharmacy.service.dto.JwtResponse;
import com.pharmacy.service.dto.LoginRequest;
import com.pharmacy.service.dtoexternal.UserNotificationDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class SchedulerTasks {
    private final LoginRequest loginRequest;
    private final AuthClient authClient;
    private final NotificationClient notificationClient;
    private JwtResponse jwtResponse;

    public SchedulerTasks(AuthClient authClient, NotificationClient notificationClient, String userName, String password) {
        this.authClient = authClient;
        this.notificationClient = notificationClient;
        this.loginRequest = new LoginRequest(userName, password);
    }

    public String login() {
        try {
            this.jwtResponse = authClient.authenticateUser(this.loginRequest).getBody();
            assert jwtResponse != null;
            return jwtResponse.getType() + " " + jwtResponse.getToken();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendScheduledNotification(List<UserNotificationDto> notifyList){
        String token = this.login();
        if(token != null) {
            this.notificationClient.createUserNotification(token, notifyList);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public void logout(){
        this.authClient.logout(this.jwtResponse.getType()+" "+this.jwtResponse.getToken());
    }
}
