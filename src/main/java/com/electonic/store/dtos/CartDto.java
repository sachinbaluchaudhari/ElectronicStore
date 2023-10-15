package com.electonic.store.dtos;

import com.electonic.store.entities.CartItem;
import com.electonic.store.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private String id;
    private Date createdDate;
    private int totalQuantity;
    private int totalPrice;
    private User user;
    private List<CartItemDto> cartItem=new ArrayList<>();

}
