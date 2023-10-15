package com.electonic.store.entities;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orderItems")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;
    private int quantity;
    private int totalPrice;
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    private Product product;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
