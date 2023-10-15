package com.electonic.store.services;

import com.electonic.store.dtos.OrderDto;
import com.electonic.store.helper.PageableResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService {
    //create order
    OrderDto createOrder(OrderDto orderDto,String userId,String cardId );
    //Remove order
    void removeOrder(String orderId);
    //get all order
    PageableResponse<OrderDto> getAllOrder(int pageNumber,int pageSize,String sortBy,String sortDir);
    //get all order of user
    List<OrderDto> getOrderByUser(String userId);
    PageableResponse<OrderDto> getOrderByOrderStatus(String orderStatus,int pageNumber,int pageSize,String sortBy,String sortDir);
    OrderDto updateOrder(OrderDto orderDto,String orderId);

}
