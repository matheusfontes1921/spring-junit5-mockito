package com.appsdeveloperblog.tutorials.junit.io;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;
import java.util.UUID;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
public class UserEntityIntegrationTest {
    UserEntity userEntity;
    @Autowired
    private TestEntityManager testEntityManager;
    @BeforeEach
    void setUp(){
        userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Gabriel");
        userEntity.setLastName("Fontes");
        userEntity.setEmail("gabriel@test.com");
        userEntity.setEncryptedPassword("123456789");
    }
    @Test
    @Order(1)
    void testUserEntity_whenValidUserDetailsAreProvided_shouldReturnStoredUserData(){
        //Arrange


        //Act
        UserEntity userEntityPersisted = testEntityManager.persistAndFlush(userEntity);

        //Assert
        Assertions.assertTrue(userEntityPersisted.getId() > 0);
        Assertions.assertEquals(userEntity.getUserId(), userEntityPersisted.getUserId(),"ID is not equal");
        Assertions.assertEquals(userEntity.getFirstName(), userEntityPersisted.getFirstName(), "First name is not equal");
    }

    @Test
    @Order(2)
    void testUserEntity_whenFirstNameIsTooLong_shouldThrowException(){
        //Arrange
        userEntity.setFirstName("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
        //Assert & Act
        Assertions.assertThrows(PersistenceException.class, () -> {
            testEntityManager.persistAndFlush(userEntity);
        },"Exception should be thrown");
    }
    @Test
    @Order(3)
    void testUserEntity_whenValidUserIdIsProvided_shouldPersist(){
        //Arrange
        userEntity.setUserId("1921");
        //Act
        UserEntity userEntityPersisted = testEntityManager.persist(userEntity);
        //Assert
        Assertions.assertEquals(userEntity.getUserId(),userEntityPersisted.getUserId(), "User ID should be equal");
    }
    @Test
    @Order(4)
    void testUserEntity_whenUserIdIsEqualToAnotherUserId_shouldThrowException(){
        userEntity.setUserId("1921");
        Assertions.assertThrows(PersistenceException.class, () -> {
            testEntityManager.persist(userEntity);
        }, "Should return an exception");
    }
}
