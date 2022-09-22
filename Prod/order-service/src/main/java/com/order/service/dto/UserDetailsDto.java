package com.order.service.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDetailsDto {
    private Long userId;

    @NotBlank(message = "Full name cannot be blank or null")
    private String fullName;

    @Size(max = 12, message = "Phone number should have exactly 12 digits")
    private String mobileNumber;

    private AddressDto address;
}
