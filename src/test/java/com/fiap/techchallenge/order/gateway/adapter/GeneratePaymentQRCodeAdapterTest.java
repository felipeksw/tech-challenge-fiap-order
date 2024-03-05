package com.fiap.techchallenge.order.gateway.adapter;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

import com.fiap.techchallenge.order.gateway.exception.GeneratePaymentQRCodeException;
import com.fiap.techchallenge.order.gateway.port.GeneratePaymentQRCodePort;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneratePaymentQRCodeAdapterTest {

    private GeneratePaymentQRCodeAdapter generatePaymentQRCodeAdapter;
    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        generatePaymentQRCodeAdapter = new GeneratePaymentQRCodeAdapter(baseUrl);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void when_generateQrCodeWithResponseCode200_then_ReturnQrCode() throws Exception {
        // Arrange
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"qrCode\": \"123456789\"}")
                .addHeader("Content-Type", "application/json"));
        GeneratePaymentQRCodePort.Request request = GeneratePaymentQRCodePort.Request.builder()
                .orderId(1L)
                .quantity(2L)
                .price(new BigDecimal(23.99))
                .title("The title")
                .build();

        // Act
        String qrCode = generatePaymentQRCodeAdapter.generate(request);

        // Assert
        assertEquals("123456789", qrCode);
    }

    @Test
    void when_generateQrCodeWithResponseCode500_then_ExceptionThrown() throws InterruptedException {
        // Arrange
        mockWebServer.enqueue(new MockResponse().setResponseCode(500)); // Simulating server error
        GeneratePaymentQRCodePort.Request request = GeneratePaymentQRCodePort.Request.builder()
                .orderId(1L)
                .quantity(2L)
                .price(new BigDecimal(23.99))
                .title("The title")
                .build();

        // Act & Assert
        Assertions.assertThrows(GeneratePaymentQRCodeException.class, () -> {
            generatePaymentQRCodeAdapter.generate(request);
        });
    }

}
