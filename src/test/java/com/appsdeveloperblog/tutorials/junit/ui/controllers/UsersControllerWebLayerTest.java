package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import com.appsdeveloperblog.tutorials.junit.ui.request.UserDetailsRequestModel;
import com.appsdeveloperblog.tutorials.junit.ui.response.UserRest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.junit.jupiter.api.Assertions;

import java.io.UnsupportedEncodingException;

@WebMvcTest(controllers = UsersController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
//@AutoConfigureMockMvc(addFilters = false)
public class UsersControllerWebLayerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("User can be created")
    void testCreateUser_whenValidUserDetailsProvided_returnsCreatedUserDetails() throws Exception {
        UserDetailsRequestModel userDetailsRequestModel = new UserDetailsRequestModel();
        userDetailsRequestModel.setFirstName("Matheus");
        userDetailsRequestModel.setLastName("Silva");
        userDetailsRequestModel.setEmail("test@test.com");
        userDetailsRequestModel.setPassword("12345678");
        userDetailsRequestModel.setRepeatPassword("12345678");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));

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
}
