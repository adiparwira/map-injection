package com.eraclouds.map_injection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    private final PaymentServiceFactory paymentServiceFactory;

    public PaymentController(PaymentServiceFactory paymentServiceFactory) {
        this.paymentServiceFactory = paymentServiceFactory;
    }

    @PostMapping("/pay")
    public ResponseEntity<String> pay(@RequestParam String type) {
        PaymentService paymentService = paymentServiceFactory.getPaymentService(type);
        if (paymentService == null) {
            return ResponseEntity.badRequest().body("Invalid payment type");
        }
        paymentService.pay();
        return ResponseEntity.ok("Payment processed with " + type);
    }
}
