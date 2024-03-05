package com.fiap.techchallenge.order.application.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.techchallenge.order.application.exceptions.GetProductException;
import com.fiap.techchallenge.order.domain.entity.Product;
import com.fiap.techchallenge.order.gateway.entity.ProductEntity;
import com.fiap.techchallenge.order.gateway.mappers.ProductMapper;
import com.fiap.techchallenge.order.gateway.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class DefaultProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private DefaultProductService productService;

    @Test
    public void testFindProduct_ProductFound() throws GetProductException {
        // Arrange
        Long productId = 1L;
        ProductEntity productEntity = createDefaultProductEntity(productId);
        Product product = createDefaultProduct(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productMapper.toDomain(productEntity)).thenReturn(product);

        // Act
        Product foundProduct = productService.findProduct(productId);

        // Assert
        assertNotNull(foundProduct);
        assertEquals(productId, foundProduct.getId());
        verify(productRepository, times(1)).findById(productId);
        verify(productMapper, times(1)).toDomain(productEntity);
    }

    @Test
    public void testFindProduct_ProductNotFound() throws GetProductException {
        // Arrange
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Product foundProduct = productService.findProduct(productId);

        // Assert
        assertNull(foundProduct);
        verify(productRepository, times(1)).findById(productId);
        verify(productMapper, never()).toDomain(any());
    }

    @Test
    public void testFindProduct_ExceptionThrown() {
        // Arrange
        Long productId = 1L;
        when(productRepository.findById(productId)).thenThrow(new RuntimeException("Simulated exception"));

        // Act & Assert
        assertThrows(GetProductException.class, () -> {
            productService.findProduct(productId);
        });
    }

    private Product createDefaultProduct(Long id) {
        return Product.builder()
                .id(id)
                .category("Lanche")
                .description("X-Salada")
                .price(new BigDecimal("22.99"))
                .build();
    }

    private ProductEntity createDefaultProductEntity(Long id) {
        return ProductEntity.builder()
                .id(id)
                .category("Lanche")
                .description("X-Salada")
                .price(new BigDecimal("22.99"))
                .build();
    }

}
