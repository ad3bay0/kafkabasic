package com.ad3bayo.kafkabasic.dto;

public record PaymentRequest(String userId, double amount, String currency) {
}
