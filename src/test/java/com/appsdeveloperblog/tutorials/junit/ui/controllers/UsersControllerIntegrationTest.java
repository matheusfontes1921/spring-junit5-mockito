package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import javax.validation.Valid;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) /*allow integration tests to run in paralel*/
//@TestPropertySource(locations = "/application-test.properties", properties = {"server.port=8081"})/*this value will override the value in application.properties*/
public class UsersControllerIntegrationTest {
    /*annotation and variable created to verify the current server port value*/
    @Value("${server.port}")
    private int serverPort;
    @LocalServerPort
    private int localServerPort;
    @Test
    void testCreateUser_whenValidDetailsAreProvided_returnsUserDetails() throws JSONException {
        //Arrange
        JSONObject userDetailsRequestJson = new JSONObject();
        userDetailsRequestJson.put("firstName", "Gabriel");
        userDetailsRequestJson.put("lastName", "Fontes");
        userDetailsRequestJson.put("email", "gabriel@test.com");
        userDetailsRequestJson.put("password", "123456789");
        userDetailsRequestJson.put("repeatPassword", "123456789");

    }

}
