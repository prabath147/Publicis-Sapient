package com.pharmacy.service.service.implementation;

import com.pharmacy.service.dto.ManagerDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.exception.ResourceException;
import com.pharmacy.service.model.ApprovalStatus;
import com.pharmacy.service.model.Manager;
import com.pharmacy.service.model.Store;
import com.pharmacy.service.repository.ManagerRepository;
import com.pharmacy.service.service.ManagerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ManagerServiceImplementation implements ManagerService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public ManagerDto createManager(ManagerDto managerDto) {
        managerDto.setApprovalStatus(ApprovalStatus.PENDING);
        managerDto.setRegistrationDate(java.sql.Date.valueOf(java.time.LocalDate.now()));
        Manager manager = modelMapper.map(managerDto, Manager.class);
        manager.setLastModified(java.sql.Date.valueOf(java.time.LocalDate.now()));
        try {
            return modelMapper.map(managerRepository.save(manager), ManagerDto.class);
        } catch (Exception e) {
            throw new ResourceException("Manager", "manager", managerDto, ResourceException.ErrorType.CREATED, e);
        }
    }

    @Override
    public Long noOfManagers(ApprovalStatus approvalStatus) {
        return managerRepository.countAllByApprovalStatus(approvalStatus);
    }

    @Override
    public boolean checkIfExists(Long managerId) {
        return managerRepository.existsById(managerId);
    }

    @Override
    public PageableResponse<ManagerDto> getManagers(Integer pageNumber, Integer pageSize) {
        Pageable requestedPage = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "lastModified"));
        Page<Manager> managerPage = managerRepository.findAll(requestedPage);
        return getPageableManagerResponse(managerPage);
    }

    private PageableResponse<ManagerDto> getPageableManagerResponse(Page<Manager> managerPage) {
        List<ManagerDto> managerDtoList = new ArrayList<>();
        for (Manager manager : managerPage.getContent()) {
            managerDtoList.add(modelMapper.map(manager, ManagerDto.class));
        }
        PageableResponse<ManagerDto> pageableManagerResponse = new PageableResponse<>();
        return pageableManagerResponse.setResponseData(managerDtoList, managerPage);
    }

    @Override
    public ManagerDto getManagerById(Long managerId) {
        Optional<Manager> optionalManager = managerRepository.findById(managerId);
        if (optionalManager.isEmpty())
            throw new ResourceException("Manager", "ID", managerId, ResourceException.ErrorType.FOUND);
        return modelMapper.map(optionalManager.get(), ManagerDto.class);
    }

    @Override
    public PageableResponse<ManagerDto> getAllPendingRequests(Integer pageNumber, Integer pageSize) {
        Pageable requestedPage = PageRequest.of(pageNumber, pageSize);
        Page<Manager> managerPage = managerRepository.findAllByApprovalStatus(ApprovalStatus.PENDING, requestedPage);
        return getPageableManagerResponse(managerPage);
    }

    @Override
    public PageableResponse<ManagerDto> getAllApprovedManagers(Integer pageNumber, Integer pageSize) {
        Pageable requestedPage = PageRequest.of(pageNumber, pageSize);
        Page<Manager> managerPage = managerRepository.findAllByApprovalStatus(ApprovalStatus.APPROVED, requestedPage);
        return getPageableManagerResponse(managerPage);
    }

    @Override
    public String rejectPendingRequestById(Long managerId) {
        Manager manager = getManagerIfExists(managerId);
        if (manager.getApprovalStatus().equals(ApprovalStatus.PENDING)) {
            manager.setApprovalStatus(ApprovalStatus.REJECTED);
            deleteManager(managerId);
        }
        return "Manager with ID - " + managerId + " is " + ApprovalStatus.REJECTED;
    }

    @Override
    public String approvePendingRequestById(Long managerId) {
        Manager manager = getManagerIfExists(managerId);
        if (manager.getApprovalStatus().equals(ApprovalStatus.PENDING)) {
            manager.setApprovalStatus(ApprovalStatus.APPROVED);
            try {
                managerRepository.save(manager);
            } catch (Exception e) {
                throw new ResourceException("Manager", "manager", manager, ResourceException.ErrorType.CREATED, e);
            }
            return "Manager with ID - " + managerId + " is " + manager.getApprovalStatus().toString();
        } else {
            return "Manager with ID -" + managerId + " is already " + manager.getApprovalStatus().toString();
        }
    }

    private Manager getManagerIfExists(Long managerId) {
        Optional<Manager> optionalManager = managerRepository.findById(managerId);
        if (optionalManager.isEmpty())
            throw new ResourceException("Manager", "ID", managerId, ResourceException.ErrorType.FOUND);
        Manager manager = optionalManager.get();
        manager.setLastModified(java.sql.Date.valueOf(java.time.LocalDate.now()));
        return manager;
    }

    @Override
    public ManagerDto updateManager(ManagerDto managerDto) {
        Manager newManager = modelMapper.map(managerDto, Manager.class);
        Optional<Manager> optionalManager = managerRepository.findById(newManager.getManagerId());
        if (optionalManager.isEmpty())
            throw new ResourceException("Manager", "manager", managerDto, ResourceException.ErrorType.FOUND);
        try {
            Manager manager = optionalManager.get();
            newManager.setLastModified(java.sql.Date.valueOf(java.time.LocalDate.now()));
            newManager.setLicenseNo(manager.getLicenseNo());
            return modelMapper.map(managerRepository.save(newManager), ManagerDto.class);
        } catch (Exception e) {
            throw new ResourceException("Manager", "manager", managerDto, ResourceException.ErrorType.UPDATED, e);
        }
    }

    @Override
    public void deleteManager(Long managerId) {
        if (!managerRepository.existsById(managerId))
            throw new ResourceException("Manager", "ID", managerId, ResourceException.ErrorType.FOUND);
        try {
            managerRepository.deleteById(managerId);
        } catch (Exception e) {
            throw new ResourceException("Manager", "ID", managerId, ResourceException.ErrorType.DELETED, e);
        }
    }

    @Override
    public PageableResponse<ManagerDto> getManagersWithFilter(String status, Integer pageNumber, Integer pageSize, String sortBy, String name) {

        Pageable requestedPage = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Manager> managerPage;
        if (status.equals("PENDING") && name.isEmpty()) {
            managerPage = managerRepository.findAllByApprovalStatus(ApprovalStatus.PENDING, requestedPage);
        } else if (status.equals("PENDING")) {
            managerPage = managerRepository.findAllByApprovalStatusAndNameContainingIgnoreCase(ApprovalStatus.PENDING, name, requestedPage);

        } else if (status.equals("APPROVED") && name.isEmpty()) {
            managerPage = managerRepository.findAllByApprovalStatus(ApprovalStatus.APPROVED, requestedPage);

        } else {
            managerPage = managerRepository.findAllByApprovalStatusAndNameContainingIgnoreCase(ApprovalStatus.APPROVED, name, requestedPage);
        }
        return getPageableManagerResponse(managerPage);

    }

}
