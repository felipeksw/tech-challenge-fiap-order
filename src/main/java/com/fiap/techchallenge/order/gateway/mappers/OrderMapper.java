package com.fiap.techchallenge.order.gateway.mappers;

import org.springframework.stereotype.Component;

import com.fiap.techchallenge.order.domain.models.Order;
import com.fiap.techchallenge.order.gateway.entity.OrderEntity;
import com.fiap.techchallenge.order.gateway.entity.OrderItemEntity;

@Component
public class OrderMapper {

    public OrderEntity toEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId())
                .client(order.getClientName())
                .customerId(order.getCustomerId())
                .paymentMethod(order.getPaymentMethod())
                .price(order.getTotalPrice())
                .status(order.getStatus().getStatus())
                .build();

        for (Order.Item item : order.getItemList()) {
            OrderItemEntity itemEntity = OrderItemEntity.builder()
                    .description(item.getDescription())
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .build();

            orderEntity.addItem(itemEntity);
        }

        return orderEntity;
    }

}
