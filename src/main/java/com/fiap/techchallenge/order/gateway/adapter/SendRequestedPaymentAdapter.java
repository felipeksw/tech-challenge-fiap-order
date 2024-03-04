package com.fiap.techchallenge.order.gateway.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.order.gateway.exception.SendRequestedPaymentException;
import com.fiap.techchallenge.order.gateway.port.SendRequestedPaymentPort;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class SendRequestedPaymentAdapter implements SendRequestedPaymentPort {

    private final String topic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public SendRequestedPaymentAdapter(@Value("${payment.requested.topic}") String topic,
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {

        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(Long orderId) throws SendRequestedPaymentException {
        try {
            Payload payload = createPayload(orderId);
            String message = objectMapper.writeValueAsString(payload);

            log.info("Sending to kafka: {}", message);

            kafkaTemplate.send(topic, message);
        } catch (JsonProcessingException e) {
            log.error("Invalid object to send to kafka");

            throw new SendRequestedPaymentException(e.getMessage());
        } catch (Exception e) {
            log.error("Error sending to kafka: {}", e.getMessage());

            throw new SendRequestedPaymentException(e.getMessage());
        }
    }

    private Payload createPayload(Long orderId) {
        return Payload.builder()
                .orderId(String.valueOf(orderId))
                .build();
    }

    @Builder
    private record Payload(
            String orderId) {
    }

}
