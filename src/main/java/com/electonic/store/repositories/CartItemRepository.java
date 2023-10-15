package com.electonic.store.repositories;

import com.electonic.store.entities.Cart;
import com.electonic.store.entities.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {


}
