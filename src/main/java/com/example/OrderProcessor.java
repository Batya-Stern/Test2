package com.example;

public class OrderProcessor {
    private final PaymentService paymentService;

    public OrderProcessor(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public boolean placeOrder(String account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        return paymentService.processPayment(account, amount);
    }
}

