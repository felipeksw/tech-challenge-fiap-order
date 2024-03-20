package com.fiap.techchallenge.order.gateway.port;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiap.techchallenge.order.gateway.exception.GeneratePaymentQRCodeException;

import lombok.Builder;
import lombok.NonNull;

public interface GeneratePaymentQRCodePort {

    byte[] generate(Request request) throws GeneratePaymentQRCodeException;

    @Builder
    public record Request(
            @JsonProperty("order_id") @NonNull String orderId,
            @NonNull String description,
            @NonNull Long quantity,
            @JsonProperty("unit_price") @NonNull BigDecimal unitPrice) {
    }

}
