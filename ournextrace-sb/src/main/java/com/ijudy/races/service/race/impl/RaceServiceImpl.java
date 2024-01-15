package com.ijudy.races.service.race.impl;

import com.ijudy.races.dto.MyRaceDTO;
import com.ijudy.races.dto.RaceDTO;
import com.ijudy.races.dto.RaceTypeDTO;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.entity.RaceEntity;
import com.ijudy.races.MyRaceStatus;
import com.ijudy.races.repository.RaceRepository;
import com.ijudy.races.repository.RaceTypeRepository;
import com.ijudy.races.service.race.RaceService;
import com.ijudy.races.util.RaceConverterUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class RaceServiceImpl implements RaceService {

    public static final Long ANONYMOUS_USER_ID = 0L;

    @Value(value = "${search.days.back}")
    private int daysBack;


    private final RaceRepository raceRepository;
    private final RaceTypeRepository raceTypeRepository;

    public RaceServiceImpl(RaceRepository raceRepository, RaceTypeRepository raceTypeRepository) {
        this.raceRepository = raceRepository;
        this.raceTypeRepository = raceTypeRepository;
    }


    @Override
    public List<MyRaceDTO> getPublicRaces() {

        final LocalDate start = LocalDate.now().minusDays(daysBack);
        final LocalDate end = LocalDate.now().plusYears(2);
        return getPublicRaces(start, end);
    }

    @Override
    public List<MyRaceDTO> getPublicRaces(LocalDate startDate, LocalDate endDate) {

        final Iterable<RaceEntity> publicRaces = raceRepository.findAllByDateBetweenOrderByDate(startDate, endDate);
        final List<MyRaceDTO>  myRaces = new ArrayList<>();

        // This is the public user.
        UserDTO PUBLIC_USER = UserDTO.builder().id(ANONYMOUS_USER_ID).build();
        publicRaces.forEach(
            raceEntity -> {
                    RaceDTO raceDTO = RaceConverterUtil.toDTO(raceEntity);
                    MyRaceDTO myRaceDTO = MyRaceDTO.builder()
                                                    .raceDTO(raceDTO)
                                                    .userDTO(PUBLIC_USER)
                                                    .myRaceStatus(MyRaceStatus.NOT_ASSIGNED)
                                                    .build();
                    myRaces.add(myRaceDTO);
                }
        );

        return myRaces;
    }
    @Override
    public Optional<RaceDTO> findById(Long raceId) {

        Optional<RaceEntity> optionalRaceEntity = raceRepository.findById(raceId);
        return optionalRaceEntity.map(RaceConverterUtil::toDTO);
    }


    @Override
    public List<RaceTypeDTO> getAllRaceTypes(){
        return RaceConverterUtil.toDTO(raceTypeRepository.findAll());
    }

    @Override
    public List<RaceDTO> getAllRaces() {

        List<RaceDTO> list = new ArrayList<>();
        for (RaceEntity raceEntity : raceRepository.findAllByOrderByDate()) {
            list.add(RaceConverterUtil.toDTO(raceEntity));
        }
        return list;
    }
}
