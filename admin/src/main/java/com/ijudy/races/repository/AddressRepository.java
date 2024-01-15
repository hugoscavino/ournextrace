package com.ijudy.races.repository;

import com.ijudy.races.entity.AddressEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<AddressEntity, Long>{

    List<AddressEntity> findByOrderByLocationAsc();

}
