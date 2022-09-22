package com.admin.service.controller;


import com.admin.service.dto.SubscriptionsDto;
import com.admin.service.dto.PageableResponse;
import com.admin.service.entity.SubscriptionStatus;
import com.admin.service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/subscription")
@Validated
@RequiredArgsConstructor
@CrossOrigin("*")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/get-subscription")
    public ResponseEntity<PageableResponse<SubscriptionsDto>> getSubscriptions(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        return new ResponseEntity<>(this.subscriptionService.getAllSubscriptions(pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/get-subscription/by-status")
    public ResponseEntity<PageableResponse<SubscriptionsDto>> getSubscriptionsByStatus(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "status", required = false) SubscriptionStatus status) {
        return new ResponseEntity<>(this.subscriptionService.getAllSubscriptionsByStatus(pageNumber, pageSize, status), HttpStatus.OK);
    }

    @GetMapping("/get-subscription/{subscription-id}")
    public ResponseEntity<SubscriptionsDto> getSubscriptionById (@PathVariable("subscription-id") Long subId){
            return new ResponseEntity<>(this.subscriptionService.getSubscriptionById(subId), HttpStatus.OK);
    }

    @PostMapping("/create-subscription")
    public ResponseEntity<SubscriptionsDto> createSubscription(@Valid @RequestBody SubscriptionsDto subscriptionsDto) throws Exception {
            return new ResponseEntity<>(this.subscriptionService.createSubscription(subscriptionsDto), HttpStatus.CREATED);
    }


    @DeleteMapping("/delete-subscription/{subscription-id}")
    public ResponseEntity<String> deleteSubscription(@PathVariable("subscription-id") Long subId) {
            return new ResponseEntity<>(this.subscriptionService.deleteSubscription(subId), HttpStatus.OK);
    }


}
