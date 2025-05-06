package com.homequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentDAO paymentDAO;

    @Autowired
    public PaymentController(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentDAO.getAllPayments();
    }

   
    @GetMapping("/{paymentID}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable String paymentID) {
        Payment p = paymentDAO.getPaymentById(paymentID);
        if (p != null) {
            return ResponseEntity.ok(p);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

 
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment created = paymentDAO.createPayment(payment);
        if (created != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    
    @PutMapping("/{paymentID}")
    public ResponseEntity<Void> updatePayment(
            @PathVariable String paymentID,
            @RequestBody Payment payment
    ) {
        payment.setPaymentID(paymentID);
        boolean updated = paymentDAO.updatePayment(payment);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

   
    @DeleteMapping("/{paymentID}")
    public ResponseEntity<Void> deletePayment(@PathVariable String paymentID) {
        boolean deleted = paymentDAO.deletePayment(paymentID);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
