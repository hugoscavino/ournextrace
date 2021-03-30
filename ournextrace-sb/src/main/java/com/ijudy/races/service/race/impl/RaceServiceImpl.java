package com.ijudy.races.service.race.impl;

import com.ijudy.races.dto.MyRaceDTO;
import com.ijudy.races.dto.RaceDTO;
import com.ijudy.races.dto.RaceTypeDTO;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.entity.AddressEntity;
import com.ijudy.races.entity.RaceEntity;
import com.ijudy.races.entity.RaceTypeEntity;
import com.ijudy.races.enums.MyRaceStatus;
import com.ijudy.races.exception.NotFoundException;
import com.ijudy.races.repository.AddressRepository;
import com.ijudy.races.repository.RaceRepository;
import com.ijudy.races.repository.RaceTypeRepository;
import com.ijudy.races.util.RaceConverterUtil;
import com.ijudy.races.service.race.RaceService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Log
public class RaceServiceImpl implements RaceService {

    public static final Long ANONYMOUS_USER_ID = 0L;

    @Value(value = "${search.days.back}")
    private int daysBack;

    @Autowired
    private final RaceRepository raceRepository;

    @Autowired
    private final AddressRepository addressRepository;

    @Autowired
    private final RaceTypeRepository raceTypeRepository;

    public RaceServiceImpl(RaceRepository raceRepository, AddressRepository addressRepository, RaceTypeRepository raceTypeRepository) {
        this.raceRepository = raceRepository;
        this.addressRepository = addressRepository;
        this.raceTypeRepository = raceTypeRepository;
    }


    @Override
    public List<MyRaceDTO> getPublicRaces() {

        final LocalDate start = LocalDate.now().minusDays(daysBack);
        final LocalDate end = LocalDate.now().plusYears(2);
        final List<MyRaceDTO> myRaces = getPublicRaces(start, end);
        return myRaces;
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
        if (optionalRaceEntity.isPresent()) {
            return Optional.of(RaceConverterUtil.toDTO(optionalRaceEntity.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public RaceDTO save(RaceDTO raceDTO) {
        RaceEntity raceEntity = RaceConverterUtil.toEntity(raceDTO);
        RaceEntity saved  = raceRepository.save(raceEntity);
        raceDTO.setId(saved.getId());
        return raceDTO;
    }

    @Override
    public RaceDTO clone(long raceId) {
        RaceDTO dto = null;

        Optional<RaceEntity> optionalRaceEntity = raceRepository.findById(raceId);
        if (optionalRaceEntity.isPresent()) {
            RaceEntity raceEntity = optionalRaceEntity.get();
            LocalDate nextYear = raceEntity.getDate().plusYears(1);
            RaceEntity clonedRace = RaceEntity.clone(raceEntity);
            clonedRace.setDate(nextYear);
            Set<RaceTypeEntity> clonedRaceTypes = RaceEntity.cloneRaceTypes(raceEntity.getRaceTypeEntities());
            clonedRace.setRaceTypeEntities(clonedRaceTypes);
            RaceEntity savedWithRaceTypes  = raceRepository.save(clonedRace);
            dto = RaceConverterUtil.toDTO(savedWithRaceTypes);
        } else {
            log.finer("Could not find RaceId : " + raceId);
        };

        return dto;
    }

    @Override
    public List<RaceTypeDTO> getAllRaceTypes(){
        return RaceConverterUtil.toDTO(raceTypeRepository.findAll());
    }

    @Override
    public RaceDTO updateRaceLocation(Long raceId, Long addressId) {

        final Optional<RaceEntity> raceEntityOptional = raceRepository.findById(raceId);
        final Optional<AddressEntity> addressEntityOptional = addressRepository.findById(addressId);

        if (raceEntityOptional.isPresent()){
            if (addressEntityOptional.isPresent()) {
                final RaceEntity raceEntity = raceEntityOptional.get();
                raceEntity.setAddress(addressEntityOptional.get());

                return RaceConverterUtil.toDTO(raceRepository.save(raceEntity));

            } else throw new NotFoundException("Address Id " + addressId + " was not found");
        } else throw new NotFoundException("Race Id " + raceId + " was not found");

    }

    @Override
    public RaceDTO delete(Long raceId) {
        RaceDTO dto = RaceDTO.builder().id(raceId).build();
        Optional<RaceEntity> entityOptional = raceRepository.findById(raceId);
        entityOptional.ifPresentOrElse(
                raceEntity -> {
                    raceRepository.delete(raceEntity);
                },
                () -> {
                    throw new NotFoundException("Race Id " + raceId + "was not found");
                }
            );
        return dto;
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
