package com.electonic.store.controller;

import com.electonic.store.dtos.UserDto;
import com.electonic.store.entities.Role;
import com.electonic.store.entities.User;
import com.electonic.store.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
public class UserControllerTest
{
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    private User user;
    private Role role;
    @BeforeEach
    public void beforeEach()
    {
        role= Role.builder().roleId("254").roleName("sachin").build();
       user= User.builder()
                .name("sachin")
                .about("i am sachin")
                .gender("male")
                .build();
    }
    @Test
    public void createUserTest() throws Exception {    mockMvc.perform(MockMvcRequestBuilders.post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ConvertObjectToJsonString(user))
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(mapper.map(user, UserDto.class));

    }

    private String ConvertObjectToJsonString(Object user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        }catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}
