package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import com.appsdeveloperblog.tutorials.junit.service.UsersService;
import com.appsdeveloperblog.tutorials.junit.service.UsersServiceImpl;
import com.appsdeveloperblog.tutorials.junit.shared.UserDto;
import com.appsdeveloperblog.tutorials.junit.ui.request.UserDetailsRequestModel;
import com.appsdeveloperblog.tutorials.junit.ui.response.UserRest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.junit.jupiter.api.Assertions;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UsersController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
//@AutoConfigureMockMvc(addFilters = false)
//@MockBean({UsersServiceImpl.class})

public class UsersControllerWebLayerTest {
    UserDetailsRequestModel userDetailsRequestModel = new UserDetailsRequestModel();
    @BeforeEach
    void init() throws JsonProcessingException {
        userDetailsRequestModel.setFirstName("Matheus");
        userDetailsRequestModel.setLastName("Silva");
        userDetailsRequestModel.setEmail("test@test.com");
        userDetailsRequestModel.setPassword("12345678");
        userDetailsRequestModel.setRepeatPassword("12345678");
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UsersService usersService;
    @Test
    @DisplayName("User can be created")
    void testCreateUser_whenValidUserDetailsProvided_returnsCreatedUserDetails() throws Exception {
        UserDto userDto = new ModelMapper().map(userDetailsRequestModel, UserDto.class);
        userDto.setUserId(UUID.randomUUID().toString());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));
        when(usersService.createUser(any(UserDto.class))).thenReturn(userDto);
//        UserDto userDto = new UserDto();
//        userDto.setFirstName("Matheus");
//        userDto.setLastName("Fontes");
//        userDto.setEmail("matheus@test.com");
//        userDto.setUserId(UUID.randomUUID().toString());

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String respondeBodyAsString = mvcResult.getResponse().getContentAsString();
        UserRest createdUser = new ObjectMapper().readValue(respondeBodyAsString,UserRest.class);

        //Assert
        Assertions.assertEquals(userDetailsRequestModel.getFirstName(),createdUser.getFirstName(),"The first name is incorrect");
        Assertions.assertEquals(userDetailsRequestModel.getLastName(), createdUser.getLastName(),"The last name is incorrect");
        Assertions.assertEquals(userDetailsRequestModel.getEmail(),createdUser.getEmail(),"The email is incorrect");
        Assertions.assertNotNull(createdUser.getUserId(),"User id should not be false");
        Assertions.assertNotEquals(userDetailsRequestModel.getPassword(), createdUser.getFirstName(),"Sua senha não pode ser seu nome");
    }
    @Test
    @DisplayName("First name is not empty")
    void testCreateUser_whenFirstNameIsNotProvided_returns400StatusCode() throws Exception {
        userDetailsRequestModel.setFirstName("");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));
        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        //Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus(), "Incorrect HTTP Status code returned");
    }

    @Test
    @DisplayName("First name is shorter than 2 characteres")
    void testCreateUser_whenFirstNameIsShorterThanTwoCharacters_returns400StatusCode() throws Exception {
        userDetailsRequestModel.setFirstName("B");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        //Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus(), "Incorrect HTTP Status code returned");

    }
}
