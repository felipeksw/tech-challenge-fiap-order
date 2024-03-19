package com.fiap.techchallenge.order.presentation.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequestMapping("/order")
@Tag(name = "Pedidos", description = "Operações para realização de pedidos")
@RequiredArgsConstructor
public class OrderController {

    private final MakeOrderUseCases makeOrderUseCases;

    @Operation(summary = "Realizar um pedido")
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
    @PostMapping
    public ResponseEntity<Response> makeOrder(@RequestBody @Valid Request orderRequest) throws Exception {

        Result orderResult = makeOrderUseCases.makeOrder(createCommand(orderRequest));

        return ResponseEntity.status(HttpStatus.OK).body(createResponse(orderResult));
    }

    public Command createCommand(Request orderRequest) {
        List<Command.OrderItem> orderItemList = List.of(orderRequest.orderItems())
                .stream()
                .map(orderItem -> Command.OrderItem.builder()
                        .productId(orderItem.productId())
                        .quantity(orderItem.quantity())
                        .additionalInfo(orderItem.additionalInfo())
                        .build())
                .collect(Collectors.toList());

        return Command.builder()
                .clientName(orderRequest.clientName())
                .customerId(orderRequest.customerId())
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
            @NotBlank(message = "O nome do cliente é obrigatório") String clientName,
            String customerId,
            @NotBlank(message = "O método de pagamento é obrigatório") String paymentMethod,
            @NotEmpty(message = "Pelo menos um item de pedido é obrigatório") @Valid OrderRequestItem[] orderItems) {

        @Builder
        public record OrderRequestItem(
                @NotNull(message = "O ID do produto é obrigatório") Long productId,
                @NotNull(message = "A quantidade do produto é obrigatória") Long quantity,
                String additionalInfo) {
        }

    }

    @Builder
    public record Response(
            Long orderId,
            String qrCode) {
    }

}
