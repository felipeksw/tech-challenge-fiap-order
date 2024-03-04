package com.fiap.techchallenge.order.domain.models;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class Product {

    private Long id;

    @NonNull private String category;
    @NonNull private String description;
    @NonNull private BigDecimal price;

}
