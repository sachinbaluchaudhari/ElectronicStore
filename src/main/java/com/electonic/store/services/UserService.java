package com.electonic.store.services;

import com.electonic.store.dtos.UserDto;
import com.electonic.store.entities.User;
import com.electonic.store.helper.PageableResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    //create
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto,String id);

    //delete
    void deleteUser(String id,String path);

    //Get all user
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get user by id
    UserDto getUserById(String id);

    //get user by emaill
    UserDto getUserByEmail(String email);

    //get user by search
    List<UserDto> getUserBySearch(String search);
    Optional<User> findByEmailForGooleWithLogin(String email);

    User saveUser(String email, String name, String photoUrl);
}
