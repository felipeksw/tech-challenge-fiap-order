package com.fiap.techchallenge.order.gateway.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.techchallenge.order.domain.entity.Order;
import com.fiap.techchallenge.order.gateway.entity.OrderEntity;
import com.fiap.techchallenge.order.gateway.entity.OrderItemEntity;

@ExtendWith(MockitoExtension.class)
public class OrderMapperTest {

    private OrderMapper orderMapper;

    @BeforeEach
    public void setUp() {
        orderMapper = new OrderMapper();
    }

    @Test
    public void testToEntity_givenValidOrder_thenReturnOrderEntity() {
        // Given
        Order order = Order.builder()
                .clientName("John Doe")
                .customerId("12345")
                .paymentMethod("qrcode")
                .build();

        order.addItem(createDefaultOrderItemBuilder().price(new BigDecimal(20.0)).build());

        // When
        OrderEntity orderEntity = orderMapper.toEntity(order);

        // Then
        assertNotNull(orderEntity);
        assertEquals(order.getId(), orderEntity.getId());
        assertEquals(order.getClientName(), orderEntity.getClient());
        assertEquals(order.getCustomerId(), orderEntity.getCustomerId());
        assertEquals(order.getPaymentMethod(), orderEntity.getPaymentMethod());
        assertEquals(order.getTotalPrice(), orderEntity.getPrice());
        assertEquals(order.getStatus().getStatus(), orderEntity.getStatus());
        assertEquals(order.getCreatedAt(), orderEntity.getCreatedAt());

        // Additional checks for order items
        assertEquals(1, orderEntity.getOrderItems().size());
        OrderItemEntity itemEntity = orderEntity.getOrderItems().get(0);
        assertNotNull(itemEntity);
        assertEquals(order.getItemList().get(0).getDescription(), itemEntity.getDescription());
        assertEquals(order.getItemList().get(0).getAdditionalInfo(), itemEntity.getAdditionalInfo());
        assertEquals(order.getItemList().get(0).getProductId(), itemEntity.getProductId());
        assertEquals(order.getItemList().get(0).getQuantity(), itemEntity.getQuantity());
    }

    Order.Item.ItemBuilder createDefaultOrderItemBuilder() {
        return Order.Item.builder()
                .price(new BigDecimal(5.0))
                .description("The description")
                .additionalInfo("The additionalInfo")
                .productId(1L)
                .quantity(1L);
    }

}
