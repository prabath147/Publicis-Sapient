package com.pharmacy.authservice.payload.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SignupRequest {
    @NotBlank(message = "username may not be empty or null")
    private String username;
    @NotBlank(message = "email may not be empty or null")
    @Email(message = "email should be of pattern user@xyz.com")
    private String email;
    private Set<String> role;
    @NotBlank(message = "password may not be empty or null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!$%^&+=])(?=\\S+$).{8,}$", message = "Must contain at least one number and one uppercase and lowercase letter and one special character, and at least 8 or more characters")
    private String password;
    private Object detailObject;

}
