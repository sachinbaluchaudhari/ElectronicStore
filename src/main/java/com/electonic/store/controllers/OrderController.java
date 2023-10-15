package com.electonic.store.controllers;

import com.electonic.store.dtos.OrderDto;
import com.electonic.store.dtos.ApiResponseMessage;
import com.electonic.store.helper.PageableResponse;
import com.electonic.store.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/{userId}/{cartId}")
    public ResponseEntity<OrderDto> createOrder(@PathVariable String userId,
                                                @PathVariable String cartId,
                                                @RequestBody OrderDto orderDto)
    {
        OrderDto order = orderService.createOrder(orderDto, userId, cartId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId)
    {
        orderService.removeOrder(orderId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("Order is deleted!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrder(  @RequestParam(name ="pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                    @RequestParam(name = "pageSize",defaultValue ="10",required = false ) int pageSize,
                                                                    @RequestParam(name = "sortBy",defaultValue = "orderDate",required = false) String sortBy,
                                                                    @RequestParam(name = "sortDir",defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<OrderDto> pageableResponse = orderService.getAllOrder(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/order-status/{orderStatus}")
    public ResponseEntity<PageableResponse<OrderDto>> getOrderByOrderStatus(@PathVariable String orderStatus,
                                                                            @RequestParam(name ="pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                             @RequestParam(name = "pageSize",defaultValue ="10",required = false ) int pageSize,
                                                                             @RequestParam(name = "sortBy",defaultValue = "orderDate",required = false) String sortBy,
                                                                             @RequestParam(name = "sortDir",defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<OrderDto> pageableResponse = orderService.getOrderByOrderStatus(orderStatus, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrderByUser(@PathVariable String userId)
    {
        List<OrderDto> orderByUser = orderService.getOrderByUser(userId);
        return new ResponseEntity<>(orderByUser,HttpStatus.OK);
    }
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable String orderId,
                                                @RequestBody OrderDto orderDto)
    {
        OrderDto order = orderService.updateOrder(orderDto, orderId);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }
}
