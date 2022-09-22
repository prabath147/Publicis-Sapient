package com.admin.service.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long addressId;

    private String street;

    private String city;

    private String state;

    private int pinCode;

    private String country;
}
