package com.electonic.store.dtos;

import com.electonic.store.entities.Order;
import com.electonic.store.entities.Role;
import com.electonic.store.validation.ImageNameValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class UserDto {
    private String id;
    @Size(min = 4,max = 25,message = "Invalid User name!!")
    @ApiModelProperty(value = "userName",name = "user name",required = true,notes = "user name of new user!")
    private String name;

   // @Pattern(regexp = "/^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$/", message = "Invalid user email!!")
    @NotBlank
    @Email(message = "Invalid User email!!")
    private String email;
    @NotBlank(message = "Enter password")
    private String password;
    @Size(min = 4,max = 6,message = "Invalid gender!!")
    private String gender;
    @NotBlank(message = "Write something about yourself!!")
    private String about;

    //Custome image validator
    @ImageNameValid
    private String imageName;
    //private List<OrderDto> orders=new ArrayList<>();
    private Set<RoleDto> roles=new HashSet<>();

}
