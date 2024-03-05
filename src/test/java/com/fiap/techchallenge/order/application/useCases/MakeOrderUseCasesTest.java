package com.fiap.techchallenge.order.application.useCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.techchallenge.order.application.exceptions.GetProductException;
import com.fiap.techchallenge.order.application.exceptions.MakeOrderException;
import com.fiap.techchallenge.order.application.services.OrderService;
import com.fiap.techchallenge.order.application.services.ProductService;
import com.fiap.techchallenge.order.domain.entity.Order;
import com.fiap.techchallenge.order.domain.entity.Product;
import com.fiap.techchallenge.order.gateway.port.GeneratePaymentQRCodePort;
import com.fiap.techchallenge.order.gateway.port.SendRequestedPaymentPort;

@ExtendWith(MockitoExtension.class)
public class MakeOrderUseCasesTest {

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @Mock
    private GeneratePaymentQRCodePort generatePaymentQRCodePort;

    @Mock
    private SendRequestedPaymentPort sendRequestedPaymentPort;

    @InjectMocks
    private MakeOrderUseCases makeOrderUseCases;

    @Test
    public void testMakeOrder_Successful() throws Exception {
        // Arrange
        MakeOrderUseCases.Command command = MakeOrderUseCases.Command.builder()
                .clientName("John Doe")
                .paymentMethod("qrCode")
                .orderItemList(Collections.singletonList(new MakeOrderUseCases.Command.OrderItem(1L, 2L)))
                .build();

        doAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.updateId(1L);
            return savedOrder;
        }).when(orderService).saveOrder(any(Order.class));
        when(productService.findProduct(1L)).thenReturn(createProduct());
        when(generatePaymentQRCodePort.generate(any())).thenReturn("123456789");

        // Act
        MakeOrderUseCases.Result result = makeOrderUseCases.makeOrder(command);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.orderId());
        assertEquals("123456789", result.qrCode());

        verify(orderService, times(1)).saveOrder(any());
        verify(sendRequestedPaymentPort, times(1)).send(result.orderId());
    }

    @Test
    public void testMakeOrder_ExceptionThrown() throws GetProductException {
        // Arrange
        MakeOrderUseCases.Command command = MakeOrderUseCases.Command.builder()
                .clientName("John Doe")
                .paymentMethod("qrCode")
                .orderItemList(Collections.singletonList(new MakeOrderUseCases.Command.OrderItem(1L, 2L)))
                .build();
        when(productService.findProduct(1L)).thenThrow(new GetProductException("Product not found"));

        // Act & Assert
        assertThrows(MakeOrderException.class, () -> {
            makeOrderUseCases.makeOrder(command);
        });
    }

    private Product createProduct() {
        return Product.builder()
                .id(1L)
                .category("Lanche")
                .description("X-Salada")
                .price(new BigDecimal("23.99"))
                .build();
    }

}
