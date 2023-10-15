package com.electonic.store.services.implement;

import com.electonic.store.dtos.UserDto;
import com.electonic.store.entities.Role;
import com.electonic.store.entities.User;
import com.electonic.store.exception.ResourceNotFoundException;
import com.electonic.store.helper.Helper;
import com.electonic.store.helper.PageableResponse;
import com.electonic.store.repositories.RoleRepository;
import com.electonic.store.repositories.UserRepository;
import com.electonic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Value("${normal.user.id}")
    private String normalId;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper mapper;
   // private String path="C:\\Users\\Dell\\OneDrive\\Desktop\\Spring Boot With Project\\ElectronicStore\\images\\user";
    @Override
    public UserDto createUser(UserDto userDto) {
        String  id = UUID.randomUUID().toString();
        userDto.setId(id);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        // dto->entity
        //mapper.map(userDto,User.class);
        User user=dtoToEntity(userDto);
        Role role = roleRepository.findById(normalId).get();
        Set<Role> roles=new HashSet<Role>();
        roles.add(role);
        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        //entity->dto
        //mapper.map(savedUser,UserDto.class);
        UserDto newDto=entityToDto(savedUser);

        return newDto;
    }


    @Override
    public UserDto updateUser(UserDto userDto, String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with given id!!!"));
       user.setName(userDto.getName());
       //update email
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setImageName(userDto.getImageName());
        userRepository.save(user);
        return entityToDto(user);
    }

    @Override
    public void deleteUser(String id,String path) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found with given id!!!"));

        //delete user profile image
        String imageName = user.getImageName();
        String fullPath=path+ File.separator+imageName;
        Path path1= Paths.get(fullPath);
        try {
            Files.delete(path1);
        }catch (Exception e)
        {

            // e.printStackTrace();
        }
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir)
    {
        Sort sort=(sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);


        return response;
    }

    @Override
    public UserDto getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with given id!"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> getUserBySearch(String search) {
        List<User> userList= userRepository.findByNameContaining(search);
        List<UserDto> userDtoList = userList.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return userDtoList;
    }

    @Override
    public Optional<User> findByEmailForGooleWithLogin(String email) {
        return this.userRepository.findByEmail(email) ;

    }
    @Value("${googlePassword}")
    private String googlePassword;

    @Override
    public User saveUser(String email, String name, String photoUrl) {
        UserDto userDto = UserDto.builder().password(googlePassword).name(name).email(email).imageName(photoUrl).build();
        System.out.println(googlePassword);
        //User user = userRepository.save(mapper.map(userDto, User.class));
        UserDto userDto1 = createUser(userDto);
        return mapper.map(userDto1,User.class);
    }

    private UserDto entityToDto(User savedUser)
    {
//        UserDto userDto = UserDto.builder()
//                .id(savedUser.getId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName()).build();
        return mapper.map(savedUser,UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .id(userDto.getId())
//                .name(userDto.getName())
//                .about(userDto.getAbout())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName()).build();
        return mapper.map(userDto,User.class);
    }

}
