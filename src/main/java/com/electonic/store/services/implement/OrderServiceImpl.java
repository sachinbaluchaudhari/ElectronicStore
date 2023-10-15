package com.electonic.store.services.implement;

import com.electonic.store.dtos.OrderDto;
import com.electonic.store.entities.*;
import com.electonic.store.exception.BadApiRequestException;
import com.electonic.store.exception.ResourceNotFoundException;
import com.electonic.store.helper.Helper;
import com.electonic.store.helper.PageableResponse;
import com.electonic.store.repositories.CartRepository;
import com.electonic.store.repositories.OrderRepository;
import com.electonic.store.repositories.UserRepository;
import com.electonic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public OrderDto createOrder(OrderDto orderDto, String userId, String cardId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id!"));
        Cart cart = cartRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("Card not found with given id!"));
        List<CartItem> cartIteams = cart.getCartIteams();
        if (cartIteams.size()==0)
        {
            throw new BadApiRequestException("Invalid number of items in cart!");
        }

        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .orderStatus(orderDto.getOrderStatus())
                .orderDate(new Date())
                .billingAddress(orderDto.getBillingAddress())
                .user(user)
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .deliveryDate(null)
                .paymentStatus(orderDto.getPaymentStatus()).build();

        AtomicInteger totalAmount=new AtomicInteger(0);
        AtomicInteger totalQuantity=new AtomicInteger(0);
        List<OrderItem> orderItems =  cartIteams.stream().map(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuanity())
                    .totalPrice(cartItem.getTotalPrice())
                    .order(order)
                    .build();
            totalAmount.set(totalAmount.get()+orderItem.getTotalPrice());
            totalQuantity.set(totalQuantity.get()+orderItem.getQuantity());
            return orderItem;

        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(totalAmount.get());

        cart.getCartIteams().clear();
        cart.setTotalQuantity(0);
        cart.setTotalPrice(0);
        cartRepository.save(cart);


        Order savedOrder =orderRepository.save(order);


        return mapper.map(savedOrder,OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with given id! "));
        orderRepository.delete(order);

    }

    @Override
    public PageableResponse<OrderDto> getAllOrder(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page = orderRepository.findAll(pageable);
        PageableResponse<OrderDto> response = Helper.getPageableResponse(page, OrderDto.class);

        return response;
    }

    @Override
    public List<OrderDto> getOrderByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id!"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map((order) -> {
            return mapper.map(order, OrderDto.class);
        }).collect(Collectors.toList());

        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrderByOrderStatus(String orderStatus, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()) :(Sort.by(sortBy).descending()) ;
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page = orderRepository.findByOrderStatus(orderStatus, pageable);
        PageableResponse<OrderDto> response = Helper.getPageableResponse(page, OrderDto.class);

        return response;
    }

    @Override
    public OrderDto updateOrder(OrderDto orderDto, String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with given id!"));
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setPaymentStatus(orderDto.getPaymentStatus());
        if (orderDto.getOrderStatus().equalsIgnoreCase("delivered")) {
            order.setDeliveryDate(new Date());
        }
        Order updatedOrder = orderRepository.save(order);
        return mapper.map(updatedOrder,OrderDto.class);
    }
}
