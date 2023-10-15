package com.electonic.store.entities;

import com.electonic.store.dtos.CartDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int quanity;
    private int totalPrice;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

}
