package com.pharmacy.service.service;

import com.pharmacy.service.dto.ManagerDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.model.ApprovalStatus;
import org.springframework.stereotype.Component;


@Component
public interface ManagerService {

    ManagerDto createManager(ManagerDto managerDto);

    Long noOfManagers(ApprovalStatus approvalStatus);

    boolean checkIfExists(Long managerId);

    PageableResponse<ManagerDto> getManagers(Integer pageNumber, Integer pageSize);

    ManagerDto getManagerById(Long managerId);

    PageableResponse<ManagerDto> getAllPendingRequests(Integer pageNumber, Integer pageSize);

    PageableResponse<ManagerDto> getAllApprovedManagers(Integer pageNumber, Integer pageSize);

    String rejectPendingRequestById(Long managerId);

    String approvePendingRequestById(Long managerId);

    ManagerDto updateManager(ManagerDto managerDto);

    void deleteManager(Long managerId);

    PageableResponse<ManagerDto> getManagersWithFilter(String status, Integer pageNumber, Integer pageSize, String sortBy, String name);
}
