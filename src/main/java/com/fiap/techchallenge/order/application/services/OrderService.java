package com.fiap.techchallenge.order.application.services;

import com.fiap.techchallenge.order.application.exceptions.SaveOrderException;
import com.fiap.techchallenge.order.domain.entity.Order;

public interface OrderService {

    Order saveOrder(Order order) throws SaveOrderException;

}
