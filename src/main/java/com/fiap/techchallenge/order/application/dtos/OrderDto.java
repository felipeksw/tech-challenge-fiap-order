package com.fiap.techchallenge.order.application.dtos;

import java.util.List;

import lombok.Builder;

@Builder
public record OrderDto(
        String clientName,
        String paymentMethod,
        List<OrderItemDto> orderItemList) {

    @Builder
    public record OrderItemDto(
            Long productId,
            Long quantity) {
    }

}
