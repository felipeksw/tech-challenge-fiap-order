package com.fiap.techchallenge.order.gateway.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client", nullable = false, columnDefinition = "varchar(255) NOT NULL")
    private String client;

    @Column(name = "customer_id", columnDefinition = "varchar(255) DEFAULT NULL")
    private String customerId;

    @Column(name = "payment_method", nullable = false, columnDefinition = "varchar(255) NOT NULL")
    private String paymentMethod;

    @Column(name = "price", nullable = false, columnDefinition = "decimal(38,2) DEFAULT 0.00")
    private BigDecimal price;

    @Column(name = "status", nullable = false, columnDefinition = "varchar(255) DEFAULT 'new'")
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "datetime DEFAULT current_timestamp()")
    private LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    public void addItem(OrderItemEntity itemEntity) {
        itemEntity.setOrder(this);
        this.orderItems.add(itemEntity);
    }

}
