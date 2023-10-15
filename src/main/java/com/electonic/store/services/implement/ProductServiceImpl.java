package com.electonic.store.services.implement;

import com.electonic.store.dtos.CategoryDto;
import com.electonic.store.dtos.ProductDto;
import com.electonic.store.entities.Category;
import com.electonic.store.entities.Product;
import com.electonic.store.exception.ResourceNotFoundException;
import com.electonic.store.helper.Helper;
import com.electonic.store.helper.PageableResponse;
import com.electonic.store.repositories.CategoryRepository;
import com.electonic.store.repositories.ProductRepository;
import com.electonic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public ProductDto create(ProductDto productDto) {
        String randomId = UUID.randomUUID().toString();
        productDto.setId(randomId);
        productDto.setAddedDate(new Date());
        Product product = mapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id!!"));
       product.setTitle(productDto.getTitle());
       product.setDescription(productDto.getDescription());
       product.setPrice(productDto.getPrice());
       product.setLive(productDto.isLive());
       product.setStock(productDto.isStock());
       product.setQuantity(productDto.getQuantity());
       product.setAddedDate(productDto.getAddedDate());
       product.setDiscountedPrice(productDto.getDiscountedPrice());
       product.setImageName(productDto.getImageName());

        Product savedProduct = productRepository.save(product);

        return mapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public void delete(String id,String path) throws IOException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id!!"));
        String imageName = product.getImageName();
        String fullPath=path+ File.separator+imageName;
        try {
            Files.delete(Path.of(fullPath));
        }catch (Exception e)
        {

        }
        productRepository.delete(product);

    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort=(sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findAll(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public ProductDto getSingle(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id!!"));
        return mapper.map(product,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> search(int pageNumber, int pageSize, String sortBy, String sortDir, String search) {
        Sort sort=(sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByTitleContaining(search,pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> live(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
        return pageableResponse;

    }

    @Override
    public ProductDto productWithCategory(ProductDto productDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id!!"));
        String randomId = UUID.randomUUID().toString();
        productDto.setId(randomId);
        productDto.setAddedDate(new Date());
        Product product = mapper.map(productDto, Product.class);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        System.out.println(savedProduct.getCategory().getId());

        ProductDto dto = mapper.map(savedProduct, ProductDto.class);
        //System.out.println(dto.getCategoryDto().getTitle());
        return dto;

    }

    @Override
    public ProductDto updateCategoryAndProduct(String productId, String categoryId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id!"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id!"));
        product.setCategory(category);
        Product updatedProduct = productRepository.save(product);
        return mapper.map(updatedProduct,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProductInCategory(String categoryId,int pageNumber, int pageSize, String sortBy,String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id!"));
        Sort sort=(sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Product>  page = productRepository.findByCategory(category,pageable);
        PageableResponse<ProductDto>  response = Helper.getPageableResponse(page, ProductDto.class);
        return response;
    }
}
