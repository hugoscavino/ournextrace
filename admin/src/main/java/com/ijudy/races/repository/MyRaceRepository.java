package com.ijudy.races.repository;

import com.ijudy.races.entity.MyRaceCompKey;
import com.ijudy.races.entity.MyRaceEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MyRaceRepository extends CrudRepository<MyRaceEntity, MyRaceCompKey> {

    /**
     * Search by the UserId to get all the user's
     * races
     *
     * @param userId
     * @return List<MyRaceEntity> Set of MyRaceEntity objects where MyRaceEntity UserId
     * is equal to the Logged-in User's Id
     */
    List<MyRaceEntity> findByIdUserId(Long userId);
}
