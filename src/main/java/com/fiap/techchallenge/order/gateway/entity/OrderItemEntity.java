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

    @Column(name = "description", columnDefinition = "varchar(255) DEFAULT NULL")
    private String description;

    @Column(name = "additional_info", columnDefinition = "varchar(255) DEFAULT NULL")
    private String additionalInfo;

    @Column(name = "product_id", columnDefinition = "bigint(20) DEFAULT NULL")
    private Long productId;

    @Column(name = "quantity", columnDefinition = "bigint(20) DEFAULT NULL")
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "order_id", columnDefinition = "bigint(20) DEFAULT NULL")
    private OrderEntity order;

}
