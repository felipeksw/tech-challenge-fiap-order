package com.fiap.techchallenge.order.gateway.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import com.fiap.techchallenge.order.domain.exception.BadRequestException;
import com.fiap.techchallenge.order.domain.exception.InternalServerErrorException;
import com.fiap.techchallenge.order.domain.exception.NotFoundException;

import reactor.core.publisher.Mono;

public class WebClientHandler {

    public static ExchangeFilterFunction handler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().equals(HttpStatus.BAD_REQUEST)) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new BadRequestException(errorBody)));
            } else if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new NotFoundException(errorBody)));
            } else if (clientResponse.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new InternalServerErrorException(errorBody)));
            } else {
                return Mono.just(clientResponse);
            }
        });
    }

}
