package com.pharmacy.authservice.payload.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LoginRequest {
	@NotBlank(message = "username may not be empty or null")
	private String username;
	@NotBlank(message = "password may not be empty or null")
	private String password;

}
