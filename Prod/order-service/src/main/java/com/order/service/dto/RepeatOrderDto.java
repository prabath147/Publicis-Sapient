package com.order.service.dto;


import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RepeatOrderDto {

    private Long id;

    private Long userId;

    @NotBlank(message = "Name cannot be blank or null")
    private String name;

    @Min(value = 2, message = "Number of Deliveries should not be less than 2")
    private int numberOfDeliveries;

    @Min(value = 3, message = "Interval should not be less than 3 days")
    private int intervalInDays;

   // @FutureOrPresent(message = "Delivery date should be in present or future")
    private LocalDate deliveryDate;

    @Valid
    private AddressDto address;

    @NotEmpty(message = "There should be atleast one item in repeat order")
    private Set<ProductDto> repeatOrderItems= new HashSet<>();
}
