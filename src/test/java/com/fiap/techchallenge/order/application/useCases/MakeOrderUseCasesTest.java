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
        byte[] qrCode = new byte[] { 0x50, 0x4E, 0x47 };
        MakeOrderUseCases.Command cmd = MakeOrderUseCases.Command.builder()
                .clientName("John Doe")
                .customerId("54321")
                .paymentMethod("qrCode")
                .orderItemList(Collections.singletonList(createDefaultOrderItem()))
                .build();

        doAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.updateId(1L);
            return savedOrder;
        }).when(orderService).saveOrder(any(Order.class));
        when(productService.findProduct(1L)).thenReturn(createDefaultProduct());
        when(generatePaymentQRCodePort.generate(any())).thenReturn(qrCode);

        // Act
        MakeOrderUseCases.Result result = makeOrderUseCases.makeOrder(cmd);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.orderId());
        assertEquals(qrCode, result.qrCode());

        verify(orderService, times(1)).saveOrder(any());
        verify(sendRequestedPaymentPort, times(1)).send(result.orderId(), cmd.customerId());
    }

    @Test
    public void testMakeOrder_ExceptionThrown() throws GetProductException {
        // Arrange
        MakeOrderUseCases.Command command = MakeOrderUseCases.Command.builder()
                .clientName("John Doe")
                .customerId("54321")
                .paymentMethod("qrCode")
                .orderItemList(Collections.singletonList(createDefaultOrderItem()))
                .build();
        when(productService.findProduct(1L)).thenThrow(new GetProductException("Product not found"));

        // Act & Assert
        assertThrows(MakeOrderException.class, () -> {
            makeOrderUseCases.makeOrder(command);
        });
    }

    private Product createDefaultProduct() {
        return Product.builder()
                .id(1L)
                .category("Lanche")
                .description("X-Salada")
                .price(new BigDecimal("23.99"))
                .build();
    }

    private MakeOrderUseCases.Command.OrderItem createDefaultOrderItem() {
        return MakeOrderUseCases.Command.OrderItem.builder()
                .productId(1L)
                .quantity(2l)
                .additionalInfo("The additionalInfo")
                .build();
    }

}
