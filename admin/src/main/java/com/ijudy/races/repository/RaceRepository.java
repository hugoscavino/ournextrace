package com.ijudy.races.repository;

import com.ijudy.races.entity.RaceEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface RaceRepository extends CrudRepository<RaceEntity, Long> {

    List<RaceEntity> findAllByOrderByDate();
    List<RaceEntity> findAllByDateBetweenOrderByDate(LocalDate startDate, LocalDate endDate);

}
