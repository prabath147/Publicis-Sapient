package com.admin.service.client.pharmacy;

import com.admin.service.dto.ManagerDto;
import com.admin.service.dto.PageableResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "manager", url = "${pharmacyServiceURL}/manager")
public interface ManagerClient {

        @GetMapping("/get-manager")
        ResponseEntity<PageableResponse<ManagerDto>> getManagers(
                        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize);

        @GetMapping("/get-manager/{id}")
        ResponseEntity<ManagerDto> getManagerById(@PathVariable("id") Long managerId);

        @GetMapping("/get-manager/pending")
        ResponseEntity<PageableResponse<ManagerDto>> getAllPendingRequests(
                        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize);

        @PutMapping("/rejection/{id}")
        ResponseEntity<String> rejectPendingRequestById(@PathVariable("id") Long managerId);

        @PutMapping("/approval/{id}")
        ResponseEntity<String> approvePendingRequestById(@PathVariable("id") Long managerId);

        @DeleteMapping(value = "/delete-manager/{id}")
        ResponseEntity<String> deleteManager(@PathVariable("id") Long managerId);

        @GetMapping("/get-manager-with-filter")
        ResponseEntity<PageableResponse<ManagerDto>> getManagersByFiltering(
                        @RequestParam(required = false) String status,
                        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                        @RequestParam(defaultValue = "managerId") String sortBy,
                        @RequestParam(required = false,defaultValue = "") String name);

}
