package com.electonic.store.repositories;

import com.electonic.store.entities.Category;
import com.electonic.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,String> {
    //search product title
    Page<Product> findByTitleContaining(String subTitle, Pageable pageable);

    //find live product
    Page<Product> findByLiveTrue(Pageable pageable);

    Page<Product> findByCategory(Category category,Pageable pageable);
}
