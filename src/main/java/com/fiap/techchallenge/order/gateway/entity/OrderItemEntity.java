package com.fiap.techchallenge.order.gateway.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orderitems")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

}
