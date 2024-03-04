package com.fiap.techchallenge.order.application.services;

import com.fiap.techchallenge.order.application.exceptions.GetProductException;
import com.fiap.techchallenge.order.domain.models.Product;

public interface ProductService {

    Product getProduct(Long id) throws GetProductException;

}
