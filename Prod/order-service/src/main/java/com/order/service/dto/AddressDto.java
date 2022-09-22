package com.order.service.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressDto {
    private Long addressId;

    @NotBlank(message = "street cannot be null or blank")
    private String street;

    @NotBlank(message = "city cannot be null or blank")
    private String city;

    @NotBlank(message = "state cannot be null or blank")
    private String state;

    @NotBlank(message = "Pin code cannot be null or blank")
    @Size(min = 6, max = 6, message = "Pincode should be 6 digit number")
    private String pinCode;

    @NotBlank(message = "Country cannot be null or blank")
    private String country;
}
