package com.fiap.techchallenge.order.domain.enums;

public enum OrderStatusEnum {

    NEW("new");

    private final String status;

    OrderStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
