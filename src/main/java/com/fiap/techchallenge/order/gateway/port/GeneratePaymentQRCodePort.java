package com.fiap.techchallenge.order.gateway.port;

import java.math.BigDecimal;

import com.fiap.techchallenge.order.gateway.exception.GeneratePaymentQRCodeException;

import lombok.Builder;
import lombok.NonNull;

public interface GeneratePaymentQRCodePort {

    String generate(Request request) throws GeneratePaymentQRCodeException;

    @Builder
    public record Request(
            @NonNull Long orderId,
            @NonNull Long quantity,
            @NonNull BigDecimal price,
            @NonNull String title) {
    }

}
