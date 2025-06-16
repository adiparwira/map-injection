package com.eraclouds.map_injection;

import org.springframework.stereotype.Service;

@Service("creditcard")
public class CreditCardPaymentService implements PaymentService {

    @Override
    public void pay() {
        System.out.println("Processing credit card payment...");
        // Logic for processing credit card payment
    }
}
