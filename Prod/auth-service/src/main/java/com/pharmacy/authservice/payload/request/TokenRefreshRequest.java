package com.pharmacy.authservice.payload.request;

import javax.validation.constraints.NotBlank;

public class TokenRefreshRequest {
  @NotBlank(message = "refresh token may not be empty or null")
  private String refreshToken;

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
