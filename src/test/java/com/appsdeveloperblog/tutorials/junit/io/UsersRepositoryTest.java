package com.appsdeveloperblog.tutorials.junit.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.UUID;

@DataJpaTest
public class UsersRepositoryTest {
    UserEntity userEntity;
    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    UsersRepository usersRepository;
    @BeforeEach
    void setUp(){
        userEntity = new UserEntity();
        userEntity.setFirstName("Gabriel");
        userEntity.setLastName("Fontes");
        userEntity.setEmail("gabriel@test.com");
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setEncryptedPassword("123456789");
        testEntityManager.persistAndFlush(userEntity);
    }

    @Test
    void testFindByEmail_whenValidEmailAddressIsProvided_returnsUserEntity(){
        //Arrange


        //Act
        UserEntity userPersisted = usersRepository.findByEmail(userEntity.getEmail());

        //Assert
        Assertions.assertEquals(userEntity.getEmail(), userPersisted.getEmail(),"The email should be equal");
    }
    @Test
    void testFindByUserId_whenValidUserIdIsProvided_returnsUserEntity() {
        //Arrange

        //Act
        UserEntity userPersisted = usersRepository.findByUserId(userEntity.getUserId());

        //Assert
        Assertions.assertNotNull(userPersisted, "There is not a single user with this ID");
        Assertions.assertEquals(userEntity.getUserId(), userPersisted.getUserId(), "User ID should be equal");
    }
    @Test
    void
    testFindUserByEmailEndsWith_whenGiveEmailDomain_returnsUsersWithGivenDomain(){
        //Arrange
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setUserId(UUID.randomUUID().toString());
        userEntity1.setFirstName("Cláudia");
        userEntity1.setLastName("Moreira");
        userEntity1.setEncryptedPassword("123456789");
        userEntity1.setEmail("claudia@gmail.com");
        testEntityManager.persistAndFlush(userEntity1);

        String emailDomainName = "@test.com";

        //Act
        List<UserEntity> users = usersRepository.findUsersWithEmailEndingWith(emailDomainName);


        //Assert
        Assertions.assertTrue(users.get(0).getEmail().endsWith(emailDomainName));



    }
}
