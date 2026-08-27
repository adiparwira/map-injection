package com.eraclouds.map_injection;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class PaymentServiceFactory {
    private final Map<String, PaymentService> paymentServiceMap;

    public PaymentServiceFactory(Map<String, PaymentService> paymentServiceMap) {
        this.paymentServiceMap = paymentServiceMap;
    }

    public PaymentService getPaymentService(String type) {
        return paymentServiceMap.get(type.toLowerCase());
    }
}
