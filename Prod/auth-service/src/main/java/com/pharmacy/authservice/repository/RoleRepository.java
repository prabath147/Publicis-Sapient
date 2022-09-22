package com.pharmacy.authservice.repository;

import com.pharmacy.authservice.model.ERole;
import com.pharmacy.authservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(ERole name);

}
