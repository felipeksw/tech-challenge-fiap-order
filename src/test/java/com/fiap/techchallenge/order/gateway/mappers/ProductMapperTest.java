package com.fiap.techchallenge.order.gateway.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fiap.techchallenge.order.domain.entity.Product;
import com.fiap.techchallenge.order.gateway.entity.ProductEntity;

public class ProductMapperTest {
    private ProductMapper productMapper;

    @BeforeEach
    public void setUp() {
        productMapper = new ProductMapper();
    }

    @Test
    public void testToDomain_givenValidProductEntity_thenReturnProduct() {
        // Given
        ProductEntity productEntity = ProductEntity.builder()
            .id(1L)
            .category("The category")
            .description("The description")
            .price(new BigDecimal(10.0))
            .build();

        // When
        Product product = productMapper.toDomain(productEntity);

        // Then
        assertNotNull(product);
        assertEquals(productEntity.getId(), product.getId());
        assertEquals(productEntity.getCategory(), product.getCategory());
        assertEquals(productEntity.getDescription(), product.getDescription());
        assertEquals(productEntity.getPrice(), product.getPrice());
    }

}
