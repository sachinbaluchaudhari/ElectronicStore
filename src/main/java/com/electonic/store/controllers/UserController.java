package com.electonic.store.controllers;

import com.electonic.store.dtos.UserDto;
import com.electonic.store.dtos.ApiResponseMessage;
import com.electonic.store.dtos.ImageResponse;
import com.electonic.store.helper.PageableResponse;
import com.electonic.store.services.FileService;
import com.electonic.store.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@RestController
@RequestMapping("/user")
@Api(value = "UserController",description = "Rest Apis related to perform user operations")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Value("${user.profile.image.path}")
    private String path;
    //create user
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto)
    {
        UserDto savedUser = userService.createUser(userDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    //update user details
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String userId,
         @Valid @RequestBody UserDto userDto)
    {
        UserDto  updatedUser = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }


    //delete user details
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String id)
    {
        userService.deleteUser(id,path);
        ApiResponseMessage  message = ApiResponseMessage.builder()
                .message("User successfully deleted with given id!!")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    //get all user
    @ApiOperation(value = "get all user",response = ResponseEntity.class,tags ={ "user-controller"})
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "success | ok"),
            @ApiResponse(code = 401,message = "Unauthorized User!"),
            @ApiResponse(code = 404,message = "User information not found!"),
            @ApiResponse(code = 403,message = "Don't access Api for this user!")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(@RequestParam(name = "pageNumber",defaultValue ="0",required = false)  int pageNumber,
                                                                @RequestParam(name = "pageSize",defaultValue ="10",required = false) int pageSize,
                                                                @RequestParam(name = "sortBy",defaultValue = "name",required = false) String sortBy,
                                                                @RequestParam(name = "sortDir",defaultValue = "asc",required = false) String sortDir)
    {
        return new ResponseEntity<>(userService.getAllUser(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }


    //get single user
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId)
    {
        UserDto userDto = userService.getUserById(userId);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }


    //get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email)
    {
        return new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
    }

    //get users by search keyword
    @GetMapping("/search/{search}")
    public ResponseEntity<List<UserDto>> getUserBySerch(@PathVariable String search)
    {
        List<UserDto> userBySearch = userService.getUserBySearch(search);
        return new ResponseEntity<>(userBySearch,HttpStatus.OK);
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<ImageResponse> uploadFile(@RequestBody MultipartFile image,
                                       @PathVariable("id") String id) throws IOException {
        String fileName = fileService.uploadFile(image, path);
        UserDto userDto = userService.getUserById(id);
        userDto.setImageName(fileName);
        userService.updateUser(userDto,id);
        ImageResponse  response = ImageResponse.builder()
                .fileName(fileName)
                .success(true)
                .message("Image uploaded!!")
                .status(HttpStatus.CREATED).build();
    return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @GetMapping("/image/{id}")
    public void serveFile(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        UserDto  user = userService.getUserById(id);
        InputStream resource = fileService.getResource(path, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());


    }
}
