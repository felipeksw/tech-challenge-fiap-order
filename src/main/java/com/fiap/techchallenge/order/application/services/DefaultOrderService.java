package com.fiap.techchallenge.order.application.services;

import org.springframework.stereotype.Service;

import com.fiap.techchallenge.order.application.exceptions.SaveOrderException;
import com.fiap.techchallenge.order.domain.entity.Order;
import com.fiap.techchallenge.order.gateway.entity.OrderEntity;
import com.fiap.techchallenge.order.gateway.mappers.OrderMapper;
import com.fiap.techchallenge.order.gateway.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultOrderService implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order saveOrder(Order order) throws SaveOrderException {
        try {
            OrderEntity orderEntity = orderMapper.toEntity(order);
            OrderEntity savedOrderEntity = orderRepository.save(orderEntity);

            order.updateId(savedOrderEntity.getId());

            return order;
        } catch (Exception e) {
            throw new SaveOrderException(e.getMessage());
        }
    }

}
