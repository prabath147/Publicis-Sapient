package com.pharmacy.authservice.repository;

import com.pharmacy.authservice.model.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserMasterRepository extends JpaRepository<UserMaster, Long> {
    UserMaster findByUsername(String username);

    UserMaster findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void deleteByUsername(String username);
}
