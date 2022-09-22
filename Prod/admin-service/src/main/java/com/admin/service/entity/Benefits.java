package com.admin.service.entity;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Benefits {
    private double delivery_discount;
    private boolean one_day_delivery;
}
