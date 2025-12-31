package com.ad3bayo.kafkabasic.service.consumer;

import com.ad3bayo.kafkabasic.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaPaymentConsumer {

    private final ObjectMapper mapper;

    @KafkaListener(topics = "test-payments")
    public void processPayment(String  rawPaymentRequest){

      try {

          PaymentRequest paymentRequest = mapper.readValue(rawPaymentRequest, PaymentRequest.class);

          log.info("---PAYMENT RECEIVED----");
          log.info("User ID: {}", paymentRequest.userId());
          log.info("Amount: {}", paymentRequest.amount());
          log.info("Currency: {}", paymentRequest.currency());
          log.info("-----------------------");

      }catch (Exception ex){

          log.error("Error parsing and processing payment record", ex);
      }
    }
}
