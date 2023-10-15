package com.electonic.store.dtos;

import com.electonic.store.entities.Category;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDto {
    private String id;
    @NotBlank(message = "Title is required!!")
    @Size(min = 4, message = "Title must be minimum 4 character require!!")
    private String title;
    @NotBlank(message = "Description is required!!")
    @Size(min = 4, message = "Description must be minimum 4 character require!!")
    private  String description;
   //@NotBlank(message = "Price is required!!")
    private int price;
    private int discountedPrice;
   //@NotBlank(message = "Quantity is required!!")
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String imageName;
   //private CategoryDto category;
}
