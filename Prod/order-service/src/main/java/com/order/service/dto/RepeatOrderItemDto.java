package com.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RepeatOrderItemDto {

	private Long id;

	@Valid
	private ProductDto product;

	@PositiveOrZero(message = "Quantity cannot be negative")
	private Long quantity;
}
