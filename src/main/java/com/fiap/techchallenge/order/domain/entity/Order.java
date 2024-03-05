package com.fiap.techchallenge.order.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fiap.techchallenge.order.domain.enums.OrderStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class Order {

    private Long id;
    private OrderStatusEnum status;
    private String clientName;
    private Long customerId;
    private String paymentMethod;
    private BigDecimal totalPrice;
    private List<Item> ItemList;

    @Builder(builderMethodName = "builder")
    public static Order newOrder(@NonNull String clientName, Long customerId, @NonNull String paymentMethod) {
        return new Order(null, OrderStatusEnum.NEW, clientName, customerId, paymentMethod, new BigDecimal(0.0),
                new ArrayList<>());
    }

    public void updateId(Long id) {
        Objects.requireNonNull(id);

        this.id = id;
    }

    public Order addItem(@NonNull Item item) {
        BigDecimal itemTotalPrice = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        totalPrice = totalPrice.add(itemTotalPrice);
        ItemList.add(item);

        return this;
    }

    public int getTotalItems() {
        return ItemList.size();
    }

    @Getter
    @Builder
    public static class Item {

        private Long id;

        @NonNull
        private BigDecimal price;

        private String description;

        @NonNull
        private Long productId;

        @NonNull
        private Long quantity;

    }

}
