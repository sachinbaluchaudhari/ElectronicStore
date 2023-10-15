package com.electonic.store.repositories;

import com.electonic.store.entities.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,String>
{
        //search title wise category
        List<Category> findByTitleContaining(String search);
}
