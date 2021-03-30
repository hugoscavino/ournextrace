package com.ijudy.races.repository;


import com.ijudy.races.entity.PasswordResetTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {

    /**
     * Find
     * @param token
     * @return
     */
    PasswordResetTokenEntity findByUserIdAndToken(Long userId, String token);

}

