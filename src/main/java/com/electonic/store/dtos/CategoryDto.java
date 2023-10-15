package com.electonic.store.dtos;

import com.electonic.store.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private String id;
    @NotBlank(message = "title must be filled!!")
    @Size(min = 5)
    private String title;
    @NotBlank(message = "description must be filled!!")
    //@Min(value =3,message = "description must be minimum 5 character allowed!!")
    private String description;
    private String caverImage;
    private List<ProductDto> productList=new ArrayList<>();
}
