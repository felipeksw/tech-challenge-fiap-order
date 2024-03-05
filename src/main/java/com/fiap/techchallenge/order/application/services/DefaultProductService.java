package com.fiap.techchallenge.order.application.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fiap.techchallenge.order.application.exceptions.GetProductException;
import com.fiap.techchallenge.order.domain.entity.Product;
import com.fiap.techchallenge.order.gateway.entity.ProductEntity;
import com.fiap.techchallenge.order.gateway.mappers.ProductMapper;
import com.fiap.techchallenge.order.gateway.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Product findProduct(Long id) throws GetProductException {
        try {
            Optional<ProductEntity> productEntityOp = productRepository.findById(id);
            if (productEntityOp.isEmpty()) {
                return null;
            }

            ProductEntity productEntity = productEntityOp.get();
            return productMapper.toDomain(productEntity);
        } catch (Exception e) {
            throw new GetProductException(e.getMessage());
        }
    }

}
