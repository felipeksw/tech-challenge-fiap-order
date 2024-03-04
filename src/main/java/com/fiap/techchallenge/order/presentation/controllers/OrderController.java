package com.fiap.techchallenge.order.presentation.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.techchallenge.order.application.useCases.MakeOrderUseCases;
import com.fiap.techchallenge.order.application.useCases.MakeOrderUseCases.Command;
import com.fiap.techchallenge.order.application.useCases.MakeOrderUseCases.Result;
import com.fiap.techchallenge.order.presentation.dtos.ErrorResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;

@RestController
@RequestMapping
@Tag(name = "Pedidos", description = "Operações para realização de pedidos")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private final MakeOrderUseCases makeOrderUseCases;

    @Operation(summary = "Atualizar status da ordem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido realizado", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Response.class))
            }),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para a realização do pedido", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))
            }),
            @ApiResponse(responseCode = "500", description = "Problemas internos durante a a realização do pedido", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))
            })
    })
    @PostMapping(value = "/order")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Response> updateOrderStatus(
            @RequestBody Request orderRequest) {

        try {
            Result orderResult = makeOrderUseCases.makeOrder(createCommand(orderRequest));

            return ResponseEntity.status(HttpStatus.OK).body(createResponse(orderResult));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public Command createCommand(Request orderRequest) {
        List<Command.OrderItem> orderItemList = List.of(orderRequest.orderItems())
                .stream()
                .map(orderItem -> Command.OrderItem.builder()
                        .productId(orderItem.productId())
                        .quantity(orderItem.quantity())
                        .build())
                .collect(Collectors.toList());

        return Command.builder()
                .clientName(orderRequest.clientName())
                .paymentMethod(orderRequest.paymentMethod())
                .orderItemList(orderItemList)
                .build();
    }

    Response createResponse(Result orderResult) {
        return Response.builder()
                .orderId(orderResult.orderId())
                .qrCode(orderResult.qrCode())
                .build();
    }

    @Builder
    public record Request(
            String clientName,
            String paymentMethod,
            OrderRequestItem[] orderItems) {

        @Builder
        public record OrderRequestItem(
                Long productId,
                Long quantity) {
        }

    }

    @Builder
    public record Response(
            Long orderId,
            String qrCode) {
    }

}
