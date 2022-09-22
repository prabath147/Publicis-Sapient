package com.pharmacy.service.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long addressId;
    @NotBlank(message = "street may not be empty or null")
    private String street;
    @NotBlank(message = "city may not be empty or null")
    private String city;
    @NotBlank(message = "state may not be empty or null")
    private String state;
    @Min(value = 100000, message = "pincode must be 6 digits")
    @Max(value = 999999, message = "pincode must be 6 digits")
    private int pinCode;
    @NotBlank(message = "country may not be empty or null")
    private String country;
}
