package com.fiap.techchallenge.order.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductTest {

    @Test
    void givenNullProductPrice_WhenBuildProduct_ThenThrownNullPointer() {
        Assertions.assertThrows(NullPointerException.class, () -> Product.builder()
                .category("Lanche")
                .description("X-Salada")
                .build());
    }

    @Test
    void when_buildingProduct_then_getProductWithCategoryDescriptionPrice() {
        Product product = Product.builder()
                .category("Lanche")
                .description("X-Salada")
                .price(new BigDecimal(22.99))
                .build();

        assertEquals("Lanche", product.getCategory());
        assertEquals("X-Salada", product.getDescription());
        assertEquals(new BigDecimal(22.99), product.getPrice());
    }

}
