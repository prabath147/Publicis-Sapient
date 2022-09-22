package com.pharmacy.service.controller;

import com.pharmacy.service.dto.ManagerDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.model.ApprovalStatus;
import com.pharmacy.service.service.ManagerService;
import com.pharmacy.service.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/pharmacy/manager")
public class ManagerController {


    @Autowired
    private ManagerService managerService;

    @PostMapping("/create-manager/{id}")
    public ResponseEntity<ManagerDto> saveManager(@PathVariable("id") Long userId, @Valid @RequestBody ManagerDto managerDto) {
        managerDto.setManagerId(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(managerService.createManager(managerDto));
    }

    // NEW
    @GetMapping("/total-managers/approved")
    public ResponseEntity<Long> noOfApprovedManagers() {
        return ResponseEntity.ok().body(managerService.noOfManagers(ApprovalStatus.APPROVED));
    }

    @GetMapping("/get-manager")
    public ResponseEntity<PageableResponse<ManagerDto>> getManagers(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(managerService.getManagers(pageNumber, pageSize));
    }

    @GetMapping("/get-manager/{id}")
    public ResponseEntity<ManagerDto> getManagerById(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long managerId) {
        if (JwtUtils.verifyId(jwt, managerId, false)) {
            return ResponseEntity.status(HttpStatus.OK).body(managerService.getManagerById(managerId));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only see their own details");
    }

    @GetMapping("/get-manager/pending")
    public ResponseEntity<PageableResponse<ManagerDto>> getAllPendingRequests(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(managerService.getAllPendingRequests(pageNumber, pageSize));
    }

    @GetMapping("/get-manager/approved")
    public ResponseEntity<PageableResponse<ManagerDto>> getAllApprovedManagers(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(managerService.getAllApprovedManagers(pageNumber, pageSize));
    }

    @PutMapping("/rejection/{id}")
    public ResponseEntity<String> rejectPendingRequestById(@PathVariable("id") Long managerId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(managerService.rejectPendingRequestById(managerId));
    }

    @PutMapping("/approval/{id}")
    public ResponseEntity<String> approvePendingRequestById(@PathVariable("id") Long managerId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(managerService.approvePendingRequestById(managerId));
    }

    @PutMapping("/update-manager/{id}")
    public ResponseEntity<ManagerDto> updateManager(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long userId, @Valid @RequestBody ManagerDto managerDto) {
        if (JwtUtils.verifyId(jwt, userId, true)) {
            managerDto.setManagerId(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(managerService.updateManager(managerDto));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only see their own details");
    }

    @DeleteMapping(value = "/delete-manager/{id}")
    public ResponseEntity<String> deleteManager(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long managerId) {
        if (JwtUtils.verifyId(jwt, managerId, false)) {
            managerService.deleteManager(managerId);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only delete their own details");
    }

    @GetMapping("/get-manager-with-filter")
    public ResponseEntity<PageableResponse<ManagerDto>> getManagersByFiltering(@RequestParam(required = false) String status, @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize, @RequestParam(defaultValue = "managerId") String sortBy, @RequestParam(required = false, defaultValue = "") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(managerService.getManagersWithFilter(status, pageNumber, pageSize, sortBy, name));
    }

}
