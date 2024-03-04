package com.fiap.techchallenge.order.domain.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fiap.techchallenge.order.domain.enums.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class Order {

    private Long id;
    private StatusEnum status;
    private String clientName;
    private Long customerId;
    private String paymentMethod;
    private BigDecimal totalPrice;
    private LocalDate createdAt;

    private List<Item> ItemList;

    public void updateId(Long id) {
        Objects.requireNonNull(id);

        this.id = id;
    }

    @Builder(builderMethodName = "builder")
    public static Order newOrder(@NonNull String clientName, Long customerId, @NonNull String paymentMethod) {
        return new Order(null, StatusEnum.NEW, clientName, customerId, paymentMethod, new BigDecimal(0.0),
                LocalDate.now(), new ArrayList<>());
    }

    public void addItem(@NonNull Item item) {
        BigDecimal itemTotalPrice = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        totalPrice = totalPrice.add(itemTotalPrice);
        ItemList.add(item);
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
