package com.admin.service.entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private Long addressId;
    private String street;
    private String city;
    private String state;
    private int pinCode;
    private String country;

}
