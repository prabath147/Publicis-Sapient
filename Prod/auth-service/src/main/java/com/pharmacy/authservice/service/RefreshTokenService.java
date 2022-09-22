package com.pharmacy.authservice.service;


import com.pharmacy.authservice.exception.ResourceException;
import com.pharmacy.authservice.exception.TokenRefreshException;
import com.pharmacy.authservice.model.ERole;
import com.pharmacy.authservice.model.RefreshToken;
import com.pharmacy.authservice.model.UserMaster;
import com.pharmacy.authservice.repository.RefreshTokenRepository;
import com.pharmacy.authservice.repository.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
  @Value("${com.pharmacy.authservice.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private UserMasterRepository userMasterRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(Long userId) {
    RefreshToken refreshToken = new RefreshToken();
    Optional<UserMaster> user = userMasterRepository.findById(userId);
    UserMaster userMaster = new UserMaster();
    if (user.isPresent()) {
      userMaster = user.get();
    }
    else {
      throw new ResourceException("UserMaster", "userId", userId, ResourceException.ErrorType.FOUND);
    }
    refreshToken.setUser(userMaster);
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }
    return token;
  }

  @Transactional
  public void deleteByUserId(Long userId) {
    Optional<UserMaster> user = userMasterRepository.findById(userId);
    if(user.isPresent()) {
      UserMaster userMaster = user.get();
      refreshTokenRepository.deleteByUser(userMaster);
    }
  }
}
