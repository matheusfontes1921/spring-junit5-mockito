package com.appsdeveloperblog.tutorials.junit.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

@DataJpaTest
public class UserEntityIntegrationTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Test
    void testUserEntity_whenValidUserDetailsAreProvided_shouldReturnStoredUserData(){
        //Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Gabriel");
        userEntity.setLastName("Fontes");
        userEntity.setEmail("gabriel@test.com");
        userEntity.setEncryptedPassword("123456789");

        //Act
        UserEntity userEntityPersisted = testEntityManager.persistAndFlush(userEntity);

        //Assert
        Assertions.assertTrue(userEntityPersisted.getId() > 0);
        Assertions.assertEquals(userEntity.getUserId(), userEntityPersisted.getUserId(),"ID is not equal");
        Assertions.assertEquals(userEntity.getFirstName(), userEntityPersisted.getFirstName(), "First name is not equal");
    }
}
