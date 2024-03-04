package com.fiap.techchallenge.order.gateway.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fiap.techchallenge.order.gateway.exception.GeneratePaymentQRCodeException;
import com.fiap.techchallenge.order.gateway.handler.WebClientHandler;
import com.fiap.techchallenge.order.gateway.port.GeneratePaymentQRCodePort;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class GeneratePaymentQRCodeAdapter implements GeneratePaymentQRCodePort {

    private final WebClient webClient;

    public GeneratePaymentQRCodeAdapter(@Value("${payment.url}") String paymentUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(paymentUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .filter(WebClientHandler.handler())
                .build();
    }

    public String generate(Request request) throws GeneratePaymentQRCodeException {
        try {
            Response response = webClient
                    .post()
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Response.class)
                    .block();

            return response.qrCode;
        } catch (Exception e) {
            log.error("Payment service request failed");

            throw new GeneratePaymentQRCodeException(e.getMessage());
        }
    }

    private record Response(
            String qrCode) {
    }

}
