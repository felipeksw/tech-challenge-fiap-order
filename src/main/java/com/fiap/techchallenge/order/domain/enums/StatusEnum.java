package com.fiap.techchallenge.order.domain.enums;

public enum StatusEnum {

    NEW("new");

    private String status;

    StatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
