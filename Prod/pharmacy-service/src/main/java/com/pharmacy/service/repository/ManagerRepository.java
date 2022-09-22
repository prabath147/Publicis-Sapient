package com.pharmacy.service.repository;

import com.pharmacy.service.model.ApprovalStatus;
import com.pharmacy.service.model.Manager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ManagerRepository extends JpaRepository<Manager,Long> {
    Page<Manager> findAllByApprovalStatus(ApprovalStatus approvalStatus, Pageable requestedPage);
    
    Page<Manager> findAllByApprovalStatusAndNameContainingIgnoreCase(ApprovalStatus approvalStatus,String name, Pageable requestedPage);
    
    Long countAllByApprovalStatus(ApprovalStatus approvalStatus);
}
