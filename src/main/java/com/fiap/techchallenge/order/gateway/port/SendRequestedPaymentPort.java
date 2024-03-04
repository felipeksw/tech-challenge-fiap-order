package com.fiap.techchallenge.order.gateway.port;

import com.fiap.techchallenge.order.gateway.exception.SendRequestedPaymentException;

public interface SendRequestedPaymentPort {

    void send(Long orderId) throws SendRequestedPaymentException;

}
