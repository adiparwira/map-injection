package com.eraclouds.map_injection;

import org.springframework.stereotype.Service;

@Service("paypal")
public class PayPalPaymentService implements PaymentService {

    @Override
    public void pay() {
        System.out.println("Processing PayPal payment...");
        // Logic for processing PayPal payment
    }

}
