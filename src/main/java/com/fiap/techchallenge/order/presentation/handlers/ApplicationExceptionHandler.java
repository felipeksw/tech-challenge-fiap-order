package com.fiap.techchallenge.order.presentation.handlers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fiap.techchallenge.order.application.exceptions.MakeOrderException;

import lombok.Builder;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler({
            MakeOrderException.class
    })
    public ResponseEntity<ErrorResponse> genericException(Exception e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message("Erro ao fazer o pedido")
                        .details(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Builder
    private static record ErrorResponse(
            int status,
            String message,
            String details,
            LocalDateTime timestamp) {
    }

}
