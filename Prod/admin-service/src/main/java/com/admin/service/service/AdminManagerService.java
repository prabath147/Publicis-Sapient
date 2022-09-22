package com.admin.service.service;

import com.admin.service.dto.ManagerDto;
import com.admin.service.dto.PageableResponse;
import org.springframework.stereotype.Component;

@Component
public interface AdminManagerService {
    PageableResponse<ManagerDto> getAllManagers(Integer pageNumber, Integer pageSize);

    ManagerDto getManagerById(Long managerId);

    PageableResponse<ManagerDto> getAllPendingRequests(Integer pageNumber, Integer pageSize);

    String approvePendingRequestById(Long managerId);

    String rejectPendingRequestById(Long managerId);

    PageableResponse<ManagerDto> getManagersWithFilter(String status, Integer pageNumber, Integer pageSize,
            String sortBy,String name);

}
