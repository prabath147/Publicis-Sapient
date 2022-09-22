package com.pharmacy.service.repository;

import com.pharmacy.service.model.Store;
import com.pharmacy.service.utils.ItemMini;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByManager_ManagerId(Long managerId);
    Page<Store> findAllByManager_ManagerId(Long managerId, Pageable pageable);
    Long countAllByManager_ManagerId(Long managerId);
}
