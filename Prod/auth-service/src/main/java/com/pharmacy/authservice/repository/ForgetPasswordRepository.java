package com.pharmacy.authservice.repository;

import com.pharmacy.authservice.model.ForgetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword, Long> {

    boolean existsByCode(String code);

    ForgetPassword findByCode(String code);

    @Transactional
    void deleteByCode(String token);

}
