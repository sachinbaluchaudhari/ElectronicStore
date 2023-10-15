package com.electonic.store.services;

import com.electonic.store.dtos.ProductDto;
import com.electonic.store.helper.PageableResponse;

import java.io.IOException;

public interface ProductService {
    ProductDto create(ProductDto productDto);
    ProductDto update(ProductDto productDto, String id);
    void delete(String id,String path) throws IOException;
    PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
    public ProductDto getSingle(String id);
    PageableResponse<ProductDto> search(int pageNumber, int pageSize, String sortBy, String sortDir, String search);
    PageableResponse<ProductDto> live(int pageNumber, int pageSize, String sortBy, String sortDir);
    ProductDto productWithCategory(ProductDto productDto,String categoryId);
    ProductDto updateCategoryAndProduct(String productId,String categoryId);
    PageableResponse<ProductDto> getAllProductInCategory(String categoryId,int pageNumber, int pageSize, String sortBy,String sortDir);
}
