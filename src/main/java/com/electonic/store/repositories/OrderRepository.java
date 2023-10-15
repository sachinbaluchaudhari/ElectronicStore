package com.electonic.store.repositories;

import com.electonic.store.dtos.OrderDto;
import com.electonic.store.entities.Order;
import com.electonic.store.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {
    List<Order> findByUser(User user);
    Page<Order> findByOrderStatus(String orderStatus, Pageable pageable);
}
