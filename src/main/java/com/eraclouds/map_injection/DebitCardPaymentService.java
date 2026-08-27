package com.eraclouds.map_injection;

import org.springframework.stereotype.Service;

@Service("debitcard")
public class DebitCardPaymentService implements PaymentService {

    @Override
    public void pay() {
        System.out.println("Processing debit card payment...");
        // Logic for processing debit card payment
    }

}
