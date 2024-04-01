package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.validation.Valid;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "/application-test.properties", properties = {"server.port=8081"})/*this value will override the value in application.properties*/
public class UsersControllerIntegrationTest {
    /*annotation and variable created to verify the current server port value*/
    @Value("${server.port}")
    private int serverPort;
    @Test
    void contextLoads() {
        System.out.println(serverPort);
    }
}
