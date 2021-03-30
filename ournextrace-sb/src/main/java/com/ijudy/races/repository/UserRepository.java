package com.ijudy.races.repository;

import com.ijudy.races.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    /** Find the entities by Email
     * @param email The user's email addresses
     * @return List<UserEntity>
     */
    List<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndSocialProvider(String email, String socialProvider);
}
