package com.electonic.store.dtos;

import com.electonic.store.entities.Cart;
import com.electonic.store.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private int id;
    private ProductDto product;
    private int quanity;
    private int totalPrice;
   // private Cart cart;

}
