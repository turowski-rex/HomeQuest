package com.homequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionDAO subscriptionDAO;

    @Autowired
    public SubscriptionController(SubscriptionDAO subscriptionDAO) {
        this.subscriptionDAO = subscriptionDAO;
    }

    
    @GetMapping
    public List<Subscription> getAllSubscriptions() {
        return subscriptionDAO.getAllSubscriptions();
    }

    
    @GetMapping("/{subscriptionType}")
    public ResponseEntity<Subscription> getSubscriptionByType(@PathVariable String subscriptionType) {
        Subscription sub = subscriptionDAO.getSubscriptionByType(subscriptionType);
        if (sub != null) {
            return ResponseEntity.ok(sub);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

   
    @PostMapping
    public ResponseEntity<Void> createSubscription(@RequestBody Subscription subscription) {
        boolean created = subscriptionDAO.createSubscription(subscription);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

   
    @PutMapping("/{subscriptionType}")
    public ResponseEntity<Void> updateSubscription(
            @PathVariable String subscriptionType,
            @RequestBody Subscription subscription
    ) {
        subscription.setSubscriptionType(subscriptionType);
        boolean updated = subscriptionDAO.updateSubscriptionDate(subscriptionType, subscription.getStartDate());
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    
    @DeleteMapping("/{subscriptionType}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable String subscriptionType) {
        boolean deleted = subscriptionDAO.deleteSubscription(subscriptionType);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}