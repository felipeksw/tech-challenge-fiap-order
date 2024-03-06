package com.fiap.techchallenge.order.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.order.application.exceptions.MakeOrderException;
import com.fiap.techchallenge.order.application.useCases.MakeOrderUseCases;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(OrderController.class)
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MakeOrderUseCases makeOrderUseCases;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidRequest_whenMakeOrder_thenReturnOk() throws Exception {
        // Arrange
        OrderController.Request request = OrderController.Request.builder()
                .clientName("John Doe")
                .paymentMethod("qrCode")
                .orderItems(new OrderController.Request.OrderRequestItem[] {
                        new OrderController.Request.OrderRequestItem(1L, 2L) })
                .build();
        MakeOrderUseCases.Result expectedResult = MakeOrderUseCases.Result.builder()
                .orderId(123L)
                .qrCode("123456789")
                .build();
        when(makeOrderUseCases.makeOrder(any())).thenReturn(new MakeOrderUseCases.Result(123L, "123456789"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").value(expectedResult.orderId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.qrCode").value(expectedResult.qrCode()));
    }

    @Test
    void givenInvalidRequest_whenMakeOrder_thenReturnBadRequest() throws Exception {
        // Arrange
        OrderController.Request request = OrderController.Request.builder()
                .clientName(null) // Invalid client name
                .paymentMethod("qrCode")
                .orderItems(new OrderController.Request.OrderRequestItem[] {
                        new OrderController.Request.OrderRequestItem(1L, 2L) })
                .build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenExceptionThrown_whenMakeOrder_thenReturnInternalServerError() throws Exception {
        // Arrange
        OrderController.Request request = OrderController.Request.builder()
                .clientName("John Doe")
                .paymentMethod("qrCode")
                .orderItems(new OrderController.Request.OrderRequestItem[] {
                        new OrderController.Request.OrderRequestItem(1L, 2L) })
                .build();
        when(makeOrderUseCases.makeOrder(any())).thenThrow(new MakeOrderException("Falha ao realizar pedido"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

}
