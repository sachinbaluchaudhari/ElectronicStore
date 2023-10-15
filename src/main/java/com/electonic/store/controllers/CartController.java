package com.electonic.store.controllers;

import com.electonic.store.dtos.CartDto;
import com.electonic.store.dtos.AddItemToCartRequest;
import com.electonic.store.dtos.ApiResponseMessage;
import com.electonic.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId,
                                                 @RequestBody AddItemToCartRequest addItemToCartRequest)
    {
        CartDto cartDto = cartService.addItemToCart(userId, addItemToCartRequest);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable int itemId)
    {
        cartService.removeItemFromCart(itemId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("Item removed from cart!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);

    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId)
    {
        cartService.clearCart(userId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Now cart is cleared!").build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId)
    {
        CartDto  cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto,HttpStatus.OK);
    }

}
