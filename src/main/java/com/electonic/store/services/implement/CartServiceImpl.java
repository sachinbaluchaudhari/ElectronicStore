package com.electonic.store.services.implement;

import com.electonic.store.dtos.CartDto;
import com.electonic.store.entities.Cart;
import com.electonic.store.entities.CartItem;
import com.electonic.store.entities.Product;
import com.electonic.store.entities.User;
import com.electonic.store.exception.ResourceNotFoundException;
import com.electonic.store.dtos.AddItemToCartRequest;
import com.electonic.store.repositories.CartItemRepository;
import com.electonic.store.repositories.CartRepository;
import com.electonic.store.repositories.ProductRepository;
import com.electonic.store.repositories.UserRepository;
import com.electonic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        String productId=request.getProductId();
        int quantity=request.getQuantity();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id!!"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id!"));
       Cart cart=null;
       try {
           cart = cartRepository.findByUser(user).get();

       }catch (NoSuchElementException ex)
       {
           cart=new Cart();
           cart.setId(UUID.randomUUID().toString());
           cart.setUser(user);
           cart.setCreatedDate(new Date());
           cart.setTotalPrice(product.getDiscountedPrice()*quantity);
           cart.setTotalQuantity(quantity);
       }

       //perform cart operation
        List<CartItem> cartIteams = cart.getCartIteams();

        //int totalAmount=0;
        AtomicInteger totalAmount=new AtomicInteger(0);
        //int totalQuantity=0;
        AtomicInteger totalQuantity=new AtomicInteger(0);

       AtomicBoolean update= new AtomicBoolean(false);
       //if cartItem already present then update item
         cartIteams= cartIteams.stream().map(cartItem -> {
            if (cartItem.getProduct().getId().equals(productId)) {
                //item already present in cart
                cartItem.setQuanity(quantity);
                cartItem.setTotalPrice(quantity * product.getDiscountedPrice());
                update.set(true);
            }
//            totalAmount=totalAmount+cartItem.getTotalPrice();
//            totalQuantity=totalQuantity+cartItem.getQuanity();
            totalAmount.set(totalAmount.get()+cartItem.getTotalPrice());
            totalQuantity.set(totalQuantity.get()+cartItem.getQuanity());
            return cartItem;

        }).collect(Collectors.toList());

    //    cart.setCartIteams( cartIteams);

        if (!update.get()) {
            CartItem item = CartItem.builder()
                    .product(product)
                    .quanity(quantity)
                    .cart(cart)
                    .totalPrice(product.getDiscountedPrice() * quantity).build();
            cart.getCartIteams().add(item);
            totalAmount.set(totalAmount.get()+item.getTotalPrice());
            totalQuantity.set(totalQuantity.get()+item.getQuanity());

        }

        cart.setTotalPrice(totalAmount.get());
        cart.setTotalQuantity(totalQuantity.get());
        Cart updatedCart = cartRepository.save(cart);
        System.out.println(updatedCart.getCartIteams().get(0).getProduct().getTitle());


        return mapper.map(updatedCart,CartDto.class);
    }

    //Remove item from card
    @Override
    public void removeItemFromCart(int itemId) {
        CartItem cartItem = cartItemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item not found with given id!"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found with given user!"));
        cart.getCartIteams().clear();
        cart.setTotalPrice(0);
        cart.setTotalQuantity(0);
        cartRepository.save(cart);
    }

    @Override
    public  CartDto getCartByUser(String userId ) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart is empty!"));
        return mapper.map(cart,CartDto.class);
    }
}
