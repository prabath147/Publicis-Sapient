package com.pharmacy.authservice.repository;

import com.pharmacy.authservice.model.RefreshToken;
import com.pharmacy.authservice.model.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Transactional
    List<RefreshToken> deleteAllByUser_Id(Long userId);

    void deleteByUser(UserMaster userMaster);

}
