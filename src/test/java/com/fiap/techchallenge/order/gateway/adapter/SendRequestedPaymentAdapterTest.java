package com.fiap.techchallenge.order.gateway.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.order.gateway.exception.SendRequestedPaymentException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SendRequestedPaymentAdapterTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SendRequestedPaymentAdapter sendRequestedPaymentAdapter;

    private static final String TOPIC = "test-topic";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sendRequestedPaymentAdapter, "topic", TOPIC);
    }

    @Test
    void givenValidOrderId_whenSend_thenSendsMessageToKafka() throws Exception {
        // Arrange
        Long orderId = 123L;
        String customerId = "1";
        SendRequestedPaymentAdapter.Payload payload = SendRequestedPaymentAdapter.Payload.builder()
                .orderId(String.valueOf(orderId))
                .customerId(customerId)
                .build();
        String message = "{\"orderId\":\"123\"}";
        when(objectMapper.writeValueAsString(payload)).thenReturn(message);

        // Act
        sendRequestedPaymentAdapter.send(orderId, customerId);

        // Assert
        verify(kafkaTemplate, times(1)).send(TOPIC, message);
    }

    @Test
    void givenJsonProcessingError_whenSend_thenThrowsException() throws JsonProcessingException {
        // Arrange
        Long orderId = 123L;
        String customerId = "1";
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

        // Act & Assert
        assertThrows(SendRequestedPaymentException.class, () -> {
            sendRequestedPaymentAdapter.send(orderId, customerId);
        });
    }

    @Test
    void givenGenericError_whenSend_thenThrowsException() throws Exception {
        // Arrange
        Long orderId = 123L;
        String customerId = "1";
        when(objectMapper.writeValueAsString(any())).thenThrow(RuntimeException.class);

        // Act & Assert
        assertThrows(SendRequestedPaymentException.class, () -> {
            sendRequestedPaymentAdapter.send(orderId, customerId);
        });
    }

}
