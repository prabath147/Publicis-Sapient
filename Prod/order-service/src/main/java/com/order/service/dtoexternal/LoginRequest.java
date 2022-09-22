package com.order.service.dtoexternal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LoginRequest {

	private String username;

	private String password;

}
