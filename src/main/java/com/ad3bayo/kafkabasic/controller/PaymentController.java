package com.ad3bayo.kafkabasic.controller;

import com.ad3bayo.kafkabasic.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final KafkaTemplate<String, PaymentRequest> kafkaTemplate;


    @PostMapping
    public String sendPayment(@RequestBody @Validated PaymentRequest paymentRequest){

        kafkaTemplate.send("test-payments",paymentRequest.userId(), paymentRequest);

        return "Payment sent to Kafka for processing with user ID: " + paymentRequest.userId();
    }
}
