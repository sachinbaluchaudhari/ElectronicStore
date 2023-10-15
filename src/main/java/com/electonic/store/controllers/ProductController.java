package com.electonic.store.controllers;

import com.electonic.store.dtos.ProductDto;
import com.electonic.store.dtos.ApiResponseMessage;
import com.electonic.store.dtos.ImageResponse;
import com.electonic.store.helper.PageableResponse;
import com.electonic.store.services.FileService;
import com.electonic.store.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/product")
public class ProductController {
    private String path="C:\\Users\\Dell\\OneDrive\\Desktop\\Spring Boot With Project\\ElectronicStore\\images\\product";
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto)
    {
        ProductDto savedProductDto = productService.create(productDto);
        return new ResponseEntity<>(savedProductDto, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto,@PathVariable String id)
    {
        ProductDto update = productService.update(productDto, id);
        return new ResponseEntity<>(update,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String id) throws IOException {
        productService.delete(id,path);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Product successfully deleted!!1")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(@RequestParam(name = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                      @RequestParam(name = "pageSize",defaultValue = "10",required = false) int pageSize,
                                                                      @RequestParam(name = "sortBy",defaultValue = "title",required = false) String sortBy,
                                                                      @RequestParam(name = "sortdir",defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<ProductDto>  response = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String id)
    {
        ProductDto productDto = productService.getSingle(id);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }
    @GetMapping("search/{search}")
    public ResponseEntity<PageableResponse<ProductDto>> getSearchProducts(@PathVariable String search,
                                                                        @RequestParam(name = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                      @RequestParam(name = "pageSize",defaultValue = "10",required = false) int pageSize,
                                                                      @RequestParam(name = "sortBy",defaultValue = "title",required = false) String sortBy,
                                                                      @RequestParam(name = "sortdir",defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<ProductDto>  response = productService.search(pageNumber, pageSize, sortBy, sortDir, search);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("live")
    public ResponseEntity<PageableResponse<ProductDto>> getLiveProduct(@RequestParam(name = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                      @RequestParam(name = "pageSize",defaultValue = "10",required = false) int pageSize,
                                                                      @RequestParam(name = "sortBy",defaultValue = "title",required = false) String sortBy,
                                                                      @RequestParam(name = "sortdir",defaultValue = "asc",required = false) String sortDir)
    {
        PageableResponse<ProductDto> response = productService.live(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{id}")
    public ResponseEntity<ImageResponse> uploadFile(@PathVariable String id,
                                                    @RequestParam MultipartFile image) throws IOException {
        ProductDto productDto = productService.getSingle(id);
        String  imageName = fileService.uploadFile(image, path);
        productDto.setImageName(imageName);
        ProductDto update = productService.update(productDto, id);
        System.out.println(update.getImageName());
        ImageResponse response = ImageResponse.builder()
                .fileName(imageName)
                .success(true)
                .status(HttpStatus.OK)
                .message("Image uploaded!").build();
        return new ResponseEntity<>(response,HttpStatus.OK);

    }
    @GetMapping("image/{id}")
    public void serveFile(@PathVariable String id, HttpServletResponse response) throws IOException {
        ProductDto productDto = productService.getSingle(id);
        String imageName = productDto.getImageName();
        InputStream resource = fileService.getResource(path, imageName);
        String fullPath=path+ File.separator+imageName;
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}
