package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import com.appsdeveloperblog.tutorials.junit.security.SecurityConstants;
import com.appsdeveloperblog.tutorials.junit.ui.response.UserRest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import javax.validation.Valid;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) /*allow integration tests to run in paralel*/
//@TestPropertySource(locations = "/application-test.properties", properties = {"server.port=8081"})/*this value will override the value in application.properties*/
public class UsersControllerIntegrationTest {
    /*annotation and variable created to verify the current server port value*/
    @Value("${server.port}")
    private int serverPort;
    @LocalServerPort
    private int localServerPort;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Test
    @DisplayName("User can be created")
    void testCreateUser_whenValidDetailsAreProvided_returnsUserDetails() throws JSONException {
        //Arrange
        JSONObject userDetailsRequestJson = new JSONObject();
        userDetailsRequestJson.put("firstName", "Gabriel");
        userDetailsRequestJson.put("lastName", "Fontes");
        userDetailsRequestJson.put("email", "gabriel@test.com");
        userDetailsRequestJson.put("password", "123456789");
        userDetailsRequestJson.put("repeatPassword", "123456789");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(userDetailsRequestJson.toString(),headers);

        //Act
        ResponseEntity<UserRest> createdUserDetailsEntity = testRestTemplate.postForEntity("/users", request, UserRest.class);
        UserRest createdUserDetails = createdUserDetailsEntity.getBody();

        //Assert
        Assertions.assertEquals(userDetailsRequestJson.getString("firstName"), createdUserDetails.getFirstName(),"First name is not equal");
        Assertions.assertEquals(userDetailsRequestJson.getString("email"), createdUserDetails.getEmail(), "Email is not equal");
        Assertions.assertFalse(createdUserDetails.getUserId().trim().isEmpty(), "User id should not be empty");

    }

    @Test
    @DisplayName("GET /users requires JWT")
    void testGetUsers_whenMissingJWT_returns403(){
        //Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity requestEntity = new HttpEntity(null, headers);

        //Act
        ResponseEntity<List<UserRest>> response = testRestTemplate.exchange("/users", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<UserRest>>() {
        });

        //Assert
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(),"HTTP Status code 403 should be returned");
    }

    @Test
    @DisplayName("Login works")
    void testUserLogin_whenValidUserCredentialsAreProvided_returnsJWTinAuthorizationHeader() throws JSONException {
        //Arrange
        JSONObject userLoginCredentials = new JSONObject();
        userLoginCredentials.put("email", "gabriel@test.com");
        userLoginCredentials.put("password", "123456789");
        HttpEntity<String> request = new HttpEntity<>(userLoginCredentials.toString());

        //Act
        ResponseEntity response = testRestTemplate.postForEntity("/users/login", request, null);

        //Arrange
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode(), "http status code should be 200");
        Assertions.assertNotNull(response.getHeaders().getValuesAsList(SecurityConstants.HEADER_STRING).get(0),"Response should contain Authorization header with JWT");
        Assertions.assertNotNull(response.getHeaders().getValuesAsList("UserID").get(0), "Response should contain an user id in a response header");

    }
}
