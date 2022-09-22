package com.admin.service.controller;

import com.admin.service.dto.SubscriberDto;
import com.admin.service.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@RequestMapping("/admin/subscriber")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @GetMapping("/get-subscriber/{user-id}")
    public ResponseEntity<SubscriberDto> getSubscriberBySubscriberId(@PathVariable("user-id") Long userId) {
        return new ResponseEntity<>(subscriberService.getSubscriber(userId), HttpStatus.OK);
    }

    @PostMapping(value = "/subscribe/{user-id}")
    public ResponseEntity<SubscriberDto> registerUserForSubscription(@PathVariable("user-id") Long userId, @RequestBody Long subscriptionId) {

        return ResponseEntity.status(HttpStatus.CREATED).body(subscriberService.registerUserForSubscription(userId, subscriptionId));

    }

    @PutMapping(value = "/unsubscribe/{id}")
    public ResponseEntity<String> deleteSubscriptionForSubscriber(@PathVariable("id") Long userId, @RequestBody Long subscriptionId) {
        subscriberService.removeSubscription(userId, subscriptionId);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted Subscription Successfully!");
    }


    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteSubscriber(@PathVariable("id") Long subscriberId) {
        subscriberService.deleteSubscriber(subscriberId);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted Subscriber Successfully!");
    }
    
}
