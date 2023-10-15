package com.electonic.store.controllers;

import com.electonic.store.dtos.CategoryDto;
import com.electonic.store.dtos.ProductDto;
import com.electonic.store.dtos.ApiResponseMessage;
import com.electonic.store.dtos.ImageResponse;
import com.electonic.store.helper.PageableResponse;
import com.electonic.store.services.CategoryService;
import com.electonic.store.services.FileService;
import com.electonic.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Value("${category.cover.image.path}")
    private String path;
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto)
    {
        String randomId = UUID.randomUUID().toString();
        categoryDto.setId(randomId);
        CategoryDto  savedCategoryDto = categoryService.create(categoryDto);
        return new ResponseEntity<>(savedCategoryDto, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("id") String id,
            @Valid @RequestBody  CategoryDto categoryDto)
    {
        CategoryDto updatedCategoryDto = categoryService.update(categoryDto, id);
        return new ResponseEntity<>(updatedCategoryDto,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String id) throws IOException {
        categoryService.delete(id,path);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Category successfully deleted!!")
                .status(HttpStatus.OK)
                .success(true).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
            @RequestParam(name ="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(name = "pageSize",defaultValue ="10",required = false ) int pageSize,
            @RequestParam(name = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(name = "sortDir",defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<CategoryDto> pageaable = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);

        return new ResponseEntity<>(pageaable,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable String id)
    {
        CategoryDto  categoryDto = categoryService.getSingle(id);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }
    @GetMapping("/search/{search}")
    public ResponseEntity<List<CategoryDto>> search(@PathVariable String search)
    {
        List<CategoryDto>  categoryDtoList = categoryService.search(search);
        return new ResponseEntity<>(categoryDtoList,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("image/{id}")
    public ResponseEntity<ImageResponse> uploadFile(@RequestBody MultipartFile image,
                                                    @PathVariable String id) throws IOException {
        String fileName = fileService.uploadFile(image, path);
        CategoryDto  categoryDto = categoryService.getSingle(id);
        categoryDto.setCaverImage(fileName);
        categoryService.create(categoryDto);
        ImageResponse  imageResponse = ImageResponse.builder()
                .message("Image successfully uploaded!!")
                .status(HttpStatus.CREATED)
                .success(true)
                .fileName(fileName).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }
    @GetMapping("image/{id}")
    public void serveFile(@PathVariable String id, HttpServletResponse response) throws IOException {
        CategoryDto  categoryDto = categoryService.getSingle(id);
        String caverImage = categoryDto.getCaverImage();
        InputStream resource = fileService.getResource(path, caverImage);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{categoryId}/product")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categoryId,
                                                                @RequestBody ProductDto productDto)
    {
        ProductDto productDto1 = productService.productWithCategory(productDto, categoryId);
        return new ResponseEntity<>(productDto1,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}/product/{productId}")
    public ResponseEntity<ProductDto> updateCategoryAndProduct(@PathVariable String categoryId,
                                                               @PathVariable String productId)
    {
        ProductDto productDto = productService.updateCategoryAndProduct(productId, categoryId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }
    @GetMapping("/{categoryId}/product")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProductInCategory(@PathVariable String categoryId,
                                                                @RequestParam(name ="pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                @RequestParam(name = "pageSize",defaultValue ="10",required = false ) int pageSize,
                                                                @RequestParam(name = "sortBy",defaultValue = "title",required = false) String sortBy,
                                                                @RequestParam(name = "sortDir",defaultValue = "asc",required = false) String sortDir)

    {
        PageableResponse<ProductDto>  response = productService.getAllProductInCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
