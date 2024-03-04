package com.fiap.techchallenge.order.gateway.mappers;

import org.springframework.stereotype.Component;

import com.fiap.techchallenge.order.domain.models.Product;
import com.fiap.techchallenge.order.gateway.entity.ProductEntity;

@Component
public class ProductMapper {

    public Product toDomain(ProductEntity productEntity) {
        return Product.builder()
                .id(productEntity.getId())
                .category(productEntity.getCategory())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .build();
    }

}
