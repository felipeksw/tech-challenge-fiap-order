package com.fiap.techchallenge.order.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.techchallenge.order.domain.enums.OrderStatusEnum;

@ExtendWith(MockitoExtension.class)
public class OrderTest {

    @Test
    void givenNullOrderClientName_WhenBuildOrder_ThenThrownNullPointer() {
        Assertions.assertThrows(NullPointerException.class, () -> Order.builder()
                .customerId(1L)
                .paymentMethod("qrCode")
                .build());
    }

    @Test
    void givenNullOrderPaymentMethod_WhenBuildOrder_ThenThrownNullPointer() {
        Assertions.assertThrows(NullPointerException.class, () -> Order.builder()
                .clientName("The clientName")
                .customerId(1L)
                .build());
    }

    @Test
    void givenNullItemPrice_WhenBuildItem_ThenThrownNullPointer() {
        Assertions.assertThrows(NullPointerException.class, () -> Order.Item.builder()
                .description("The description")
                .productId(1L)
                .quantity(1L)
                .build());
    }

    @Test
    void givenNullItemProductId_WhenBuildItem_ThenThrownNullPointer() {
        Assertions.assertThrows(NullPointerException.class, () -> Order.Item.builder()
                .price(new BigDecimal(1.0))
                .description("The description")
                .quantity(1L)
                .build());
    }

    @Test
    void givenNullItemPQuantity_WhenBuildItem_ThenThrownNullPointer() {
        Assertions.assertThrows(NullPointerException.class, () -> Order.Item.builder()
                .price(new BigDecimal(1.0))
                .description("The description")
                .productId(1L)
                .build());
    }

    @Test
    void when_buildingOrder_then_getOrderStatusNew() {
        Order order = Order.builder()
                .clientName("The clientName")
                .customerId(1L)
                .paymentMethod("qrCode")
                .build();

        assertEquals(OrderStatusEnum.NEW, order.getStatus());
        assertEquals("The clientName", order.getClientName());
        assertEquals(1L, order.getCustomerId());
        assertEquals("qrCode", order.getPaymentMethod());
        assertEquals(new BigDecimal(0.0), order.getTotalPrice());
        assertEquals(0, order.getItemList().size());
    }

    @Test
    void when_updatingOrderId_then_getOrderWithUpdatedId() {
        Order order = buildDefaultOrder();

        order.updateId(1L);

        assertEquals(1L, order.getId());
    }

    @Test
    void when_addingNewItems_then_getIncreasedTotalPrice() {
        Order order = buildDefaultOrder();

        order.addItem(createDefaultOrderItemBuilder().price(new BigDecimal(20.0)).build());
        order.addItem(createDefaultOrderItemBuilder().price(new BigDecimal(3.0)).quantity(2L).build());

        assertEquals(new BigDecimal(26.0), order.getTotalPrice());
        assertEquals(2, order.getItemList().size());
    }

    Order buildDefaultOrder() {
        return Order.builder()
                .clientName("The clientName")
                .customerId(1L)
                .paymentMethod("qrCode")
                .build();
    }

    Order.Item.ItemBuilder createDefaultOrderItemBuilder() {
        return Order.Item.builder()
                .price(new BigDecimal(5.0))
                .description("The description")
                .productId(1L)
                .quantity(1L);
    }
}
