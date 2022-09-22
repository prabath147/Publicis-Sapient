package com.admin.service.service.impl;

import com.admin.service.client.pharmacy.ManagerClient;
import com.admin.service.dto.ManagerDto;
import com.admin.service.dto.PageableResponse;
import com.admin.service.exception.ResourceException;
import com.admin.service.service.AdminManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AdminManagerServiceImpl implements AdminManagerService {

    @Value("${pharmacyServiceURL}")
    private String pharmacyServiceURL;

    @Autowired
    private ManagerClient managerClient;

    @Override
    public PageableResponse<ManagerDto> getAllManagers(Integer pageNumber, Integer pageSize) {

        ResponseEntity<PageableResponse<ManagerDto>> response = managerClient.getManagers(pageNumber, pageSize);
        return response.getBody();

    }

    @Override
    public ManagerDto getManagerById(Long managerId) {

        try {
            ResponseEntity<ManagerDto> response = managerClient.getManagerById(managerId);
            return response.getBody();
        } catch (Exception e) {
            throw new ResourceException("Manager", "managerId", managerId, ResourceException.ErrorType.FOUND);
        }

    }

    @Override
    public PageableResponse<ManagerDto> getAllPendingRequests(Integer pageNumber, Integer pageSize) {

        ResponseEntity<PageableResponse<ManagerDto>> response = managerClient.getAllPendingRequests(pageNumber,
                pageSize);
        return response.getBody();

    }

    @Override
    public String approvePendingRequestById(Long managerId) {
        try {
            ResponseEntity<String> response = managerClient.approvePendingRequestById(managerId);
            return response.getBody();
        }

        catch (Exception e) {
            throw new ResourceException("Manager", "managerId", managerId, ResourceException.ErrorType.FOUND);
        }
    }

    @Override
    public String rejectPendingRequestById(Long managerId) {
        try {
            ResponseEntity<String> response = managerClient.rejectPendingRequestById(managerId);
            return response.getBody();
        } catch (Exception e) {
            throw new ResourceException("Manager", "managerId", managerId, ResourceException.ErrorType.FOUND);
        }
    }

    @Override
    public PageableResponse<ManagerDto> getManagersWithFilter(String status, Integer pageNumber, Integer pageSize,
            String sortBy, String name) {
        ResponseEntity<PageableResponse<ManagerDto>> response = managerClient.getManagersByFiltering(status, pageNumber,
                pageSize, sortBy,name);
        return response.getBody();
    }

}
