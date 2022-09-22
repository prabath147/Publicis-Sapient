package com.order.service.dtoexternal;

import com.order.service.dto.OrdersDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    Long userId;
    String emailSubject;
    String emailBody;
}
