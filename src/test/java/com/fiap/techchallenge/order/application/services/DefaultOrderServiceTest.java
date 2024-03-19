package com.fiap.techchallenge.order.application.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.techchallenge.order.application.exceptions.SaveOrderException;
import com.fiap.techchallenge.order.domain.entity.Order;
import com.fiap.techchallenge.order.gateway.entity.OrderEntity;
import com.fiap.techchallenge.order.gateway.mappers.OrderMapper;
import com.fiap.techchallenge.order.gateway.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class DefaultOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private DefaultOrderService orderService;

    @Test
    void when_saveOrder_Then_ReturnOrderWithUpdatedId() throws Exception {
        // Arrange
        Order order = createDefaultOrder();
        OrderEntity orderEntity = createDefaultOrderEntity();
        when(orderMapper.toEntity(order)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

        // Act
        Order savedOrder = orderService.saveOrder(order);

        // Assert
        assertNotNull(savedOrder);
        assertEquals(1L, order.getId());
        verify(orderMapper, times(1)).toEntity(order);
        verify(orderRepository, times(1)).save(orderEntity);
    }

    @Test
    void testSaveOrder_ExceptionThrown() {
        // Arrange
        Order order = createDefaultOrder();
        when(orderMapper.toEntity(order)).thenThrow(new RuntimeException("Simulated exception"));

        // Act & Assert
        assertThrows(SaveOrderException.class, () -> {
            orderService.saveOrder(order);
        });
    }

    Order createDefaultOrder() {
        return Order.builder()
                .clientName("John Doe")
                .customerId("54321")
                .paymentMethod("qrCode")
                .build();
    }

    private OrderEntity createDefaultOrderEntity() {
        return OrderEntity.builder()
                .id(1L)
                .client("John Doe")
                .customerId("54321")
                .paymentMethod("Credit Card")
                .build();
    }

}
