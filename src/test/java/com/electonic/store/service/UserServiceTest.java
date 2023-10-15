package com.electonic.store.service;

import com.electonic.store.dtos.UserDto;
import com.electonic.store.entities.Role;
import com.electonic.store.entities.User;
import com.electonic.store.helper.PageableResponse;
import com.electonic.store.repositories.RoleRepository;
import com.electonic.store.repositories.UserRepository;
import com.electonic.store.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class UserServiceTest {
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper mapper;
    private User user;
    private Role role;
    @BeforeEach
    public void beforeEach()
    {
        role=Role.builder().roleId("2021").roleName("Normal").build();
        user=User.builder()
                .imageName("sachin.jpg")
                .name("sachin")
                .email("sac@gmail.com")
                .gender("male")
                .password("sachin")
                .about("i am sachin")
                .roles(Set.of(role)).build();
    }
    @Test
    public void createUserTest()
    {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(Mockito.anyString())).thenReturn(Optional.of(role));
        UserDto userDto = userService.createUser(mapper.map(user, UserDto.class));
        System.out.println(userDto.getName());
        Assertions.assertNotNull(userDto);


    }
    @Test
    public void updateUserTest()
    {
        String userid="";
        UserDto userDto= UserDto.builder()
                .imageName("pratik.jpg")
                .name("pratik")
                .about("i am pratik")
                .build();
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        UserDto updatedUserDto = userService.updateUser(userDto, userid);
        System.out.println(updatedUserDto.getName());
        System.out.println(updatedUserDto.getAbout());
        Assertions.assertNotNull(updatedUserDto);
        Assertions.assertEquals(userDto.getName(),updatedUserDto.getName());
    }
    @Value("${user.profile.image.path}")
    String path;
    @Test
    public void deleteUserTest()
    {
        String userId="";

        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        userService.deleteUser(userId,path);
        Mockito.verify(userRepository,Mockito.times(1)).delete(user);
    }
    @Test
    public void getAllUserTest()
    {
        List<User> userList= Arrays.asList(user,user,user);
        Page<User> page=new PageImpl<>(userList);
        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<UserDto> allUser = userService.getAllUser(0, 3, "name", "asc");
        System.out.println(allUser.getTotalElements());
        Assertions.assertEquals(3,allUser.getTotalElements());
    }
    @Test
    public void getUserByIdTest()
    {
        String userId="125545";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserDto userDto = userService.getUserById(userId);
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getName(),userDto.getName());
    }
    @Test
    public void getUserByEmailTest()
    {
        String email="sach@gmail.com";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        UserDto userByEmail = userService.getUserByEmail(email);
        Assertions.assertNotNull(userByEmail);
        Assertions.assertEquals(user.getName(),userByEmail.getName());
    }
    @Test
    public void getUserBySearchTest()
    {
        String name="sachin";
        Mockito.when(userRepository.findByNameContaining(name)).thenReturn(Arrays.asList(user,user,user));
        List<UserDto> userDtoList = userService.getUserBySearch(name);
        Assertions.assertEquals(3,userDtoList.size());

    }

}
