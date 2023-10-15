package com.electonic.store.services.implement;

import com.electonic.store.dtos.CategoryDto;
import com.electonic.store.entities.Category;
import com.electonic.store.exception.ResourceNotFoundException;
import com.electonic.store.helper.Helper;
import com.electonic.store.helper.PageableResponse;
import com.electonic.store.repositories.CategoryRepository;
import com.electonic.store.services.CategoryService;
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
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ModelMapper mapper;
    @Override
    public CategoryDto create(CategoryDto categoryDto)
    {
        Category  category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        CategoryDto  savedCategoryDto = mapper.map(savedCategory, CategoryDto.class);
        return savedCategoryDto;
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String id)
    {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id!!"));
        category.setTitle(categoryDto.getTitle());
        category.setCaverImage(categoryDto.getCaverImage());
        category.setDescription(categoryDto.getDescription());
        Category save = categoryRepository.save( category);

        return mapper.map(save,CategoryDto.class);
    }

    @Override
    public void delete(String id,String path) throws IOException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id!!"));
        String  fileName = category.getCaverImage();
        String fullPath=path+ File.separator+fileName;
        Path path1= Paths.get(fullPath);
        try {
            Files.delete(path1);
        }catch(Exception e)
        {

        }
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir)
    {
        Sort sort=(sortDir.equalsIgnoreCase("asc"))? (Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page page= categoryRepository.findAll(pageable);
        PageableResponse pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        return pageableResponse;
    }

    @Override
    public CategoryDto getSingle(String id)
    {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id!!"));

        return mapper.map(category,CategoryDto.class);
    }

    @Override
    public  List<CategoryDto> search(String search ) {
        List<Category> categoryList = categoryRepository.findByTitleContaining(search);
        List<CategoryDto>  categoryDto = categoryList.stream().map(category -> mapper.map(category, CategoryDto.class)).collect(Collectors.toList());

        return categoryDto;
    }
}
