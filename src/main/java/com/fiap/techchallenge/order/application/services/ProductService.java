package com.fiap.techchallenge.order.application.services;

import com.fiap.techchallenge.order.application.exceptions.GetProductException;
import com.fiap.techchallenge.order.domain.entity.Product;

public interface ProductService {

    Product findProduct(Long id) throws GetProductException;

}
