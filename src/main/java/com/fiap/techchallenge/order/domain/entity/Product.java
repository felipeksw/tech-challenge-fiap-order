package com.fiap.techchallenge.order.domain.entity;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class Product {

    private Long id;
    private String category;
    private String description;

    @NonNull
    private BigDecimal price;

}
