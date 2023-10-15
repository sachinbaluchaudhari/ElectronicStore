package com.electonic.store.services;

import com.electonic.store.dtos.CategoryDto;
import com.electonic.store.helper.PageableResponse;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    //create category
    public CategoryDto create(CategoryDto categoryDto);

    //update category
    public CategoryDto update(CategoryDto categoryDto,String id);

    //delete category
    public void delete(String id,String path) throws IOException;

    //get all category
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize,String sortBy, String sortDir);

    //get single category by categoryId
    public CategoryDto getSingle(String id);

    //search category
    public List<CategoryDto> search(String search);

}
