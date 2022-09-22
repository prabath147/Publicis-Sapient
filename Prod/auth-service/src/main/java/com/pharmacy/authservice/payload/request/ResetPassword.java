package com.pharmacy.authservice.payload.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ResetPassword {
    @NotBlank(message = "old password may not be empty or null")
    String oldPassword;
    @NotBlank(message = "new password may not be empty or null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!^&+=])(?=\\S+$).{8,}$", message = "Must contain at least one number and one uppercase and lowercase letter and one special character, and at least 8 or more characters")
    String newPassword;

}
