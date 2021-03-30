package com.ijudy.races.respository;

import com.ijudy.SpringIntegrationTest;
import com.ijudy.races.entity.UserEntity;
import com.ijudy.races.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityRepositoryIntegrationTest extends SpringIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test Finding One UserEntity by Id for Postgres")
    void getById()  {

        UserEntity user3 = UserEntity.builder().email("test3@aol.com").active(true).name("Name3").build();
        UserEntity savedUser = userRepository.save(user3);
        Long userId = savedUser.getId();
        Optional<UserEntity> foundUser = userRepository.findById(userId);
        assertNotNull(foundUser);
        assertFalse(foundUser.isEmpty());

        // Clean Up
        userRepository.delete(savedUser);
        foundUser = userRepository.findById( userId);
        assertFalse(foundUser.isPresent());
    }

  }
