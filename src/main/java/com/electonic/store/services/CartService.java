package com.electonic.store.services;

import com.electonic.store.dtos.CartDto;
import com.electonic.store.dtos.AddItemToCartRequest;

public interface CartService {
    //add item to cart
    //case1:cart for user is not available: we will create cart and then add the items
    //case2:cart available then add the item in cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //Remove item from card
    void removeItemFromCart(int itemId);

    //clear cart
    void clearCart(String userId);
     CartDto getCartByUser(String userId);

}
