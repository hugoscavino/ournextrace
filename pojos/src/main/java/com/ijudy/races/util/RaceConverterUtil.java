package com.ijudy.races.util;

import com.ijudy.races.dto.*;
import com.ijudy.races.entity.*;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class RaceConverterUtil {

    private static final long DEFAULT_AUTHOR_ID = 0L;

    /**
     * Convert from a Race Entity to a DTO. If Address or RaceTypes
     * are null DTO will remain null and not have an empty set.
     * @param entity The Race Entity
     * @return RaceDTO a converted Entity
     */
    public static RaceDTO toDTO(RaceEntity entity){

        AddressDTO addressDTO = null;

        AddressEntity addressEntity = entity.getAddress();
        if (addressEntity != null) {
                addressDTO = AddressDTO.builder()
                    .id(addressEntity.getId())
                    .location(addressEntity.getLocation())
                    .street(addressEntity.getStreet())
                    .city(addressEntity.getCity())
                    .state(addressEntity.getState())
                    .zip(addressEntity.getZip())
                    .country(addressEntity.getCountry())
                    .phone(addressEntity.getPhone())
                    .notes(addressEntity.getNotes())
                    .modDate(addressEntity.getModDate()).build();
        }
        Set<RaceTypeDTO> raceTypeSet = null;

        if (entity.getRaceTypeEntities() != null) {
            raceTypeSet = new HashSet<>(entity.getRaceTypeEntities().size());
            for (RaceTypeEntity raceTypeEntity : entity.getRaceTypeEntities()) {
                raceTypeSet.add(RaceTypeDTO.builder()
                        .id(raceTypeEntity.getId())
                        .desc(raceTypeEntity.getDescription())
                        .shortDesc(raceTypeEntity.getShortDesc())
                        .name(raceTypeEntity.getName())
                        .build());
            }
        }

        UserEntity author = entity.getAuthor();
        boolean isAdmin = UserDTO.isAdmin(author.getRoleEntities());
        boolean isPowerUser = UserDTO.isPowerUser(author.getRoleEntities());
        boolean isUser = UserDTO.isUser(author.getRoleEntities());
        UserDTO authorDTO = UserDTO.builder()
                            .id(author.getId())
                            .firstName(author.getFirstName())
                            .lastName(author.getLastName())
                            .name(author.getName())
                            .active(author.getActive())
                            .city(author.getCity())
                            .state(author.getState())
                            .country(author.getCountry())
                            .email(author.getEmail())
                            .isAdmin(isAdmin)
                            .isPowerUser(isPowerUser)
                            .isUser(isUser)
                            .lastUpdated(author.getLastUpdated()).build();


        return RaceDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .date(entity.getDate())
                .description(entity.getDescription())
                .address(addressDTO)
                .url(entity.getUrl())
                .isPublic(entity.getIsPublic())
                .couponCode(entity.getCouponCode())
                .modDate(entity.getModDate())
                .isActive(entity.getIsActive())
                .isCancelled(entity.getIsCancelled())
                .author(authorDTO)
                .raceTypes(raceTypeSet)
                .build();
    }

    /**
     * Convert Race DTO to an Entity
     * @param dto The Race DTO
     * @return RaceEntity a converted Race Entity
     */
    public static RaceEntity toEntity(RaceDTO dto) {

        RaceEntity raceEntity = new RaceEntity();
        raceEntity.setId(dto.getId());
        raceEntity.setName(dto.getName());
        raceEntity.setDescription(dto.getDescription());
        raceEntity.setDate(dto.getDate());
        AddressDTO addressDTO = null;
        if (dto.getAddress() == null || dto.getAddress().getId() == null){
            // Set Address to Default of 0
            addressDTO = AddressDTO.getDefaultAddress();
        } else {
            addressDTO = dto.getAddress();
        }
        raceEntity.setAddress(toEntity(addressDTO));
        raceEntity.setIsPublic(dto.isPublic());
        raceEntity.setIsActive(dto.isActive());
        raceEntity.setIsCancelled(dto.isCancelled());
        raceEntity.setCouponCode(dto.getCouponCode());
        raceEntity.setUrl(dto.getUrl());
        raceEntity.setModDate(dto.getModDate());
        raceEntity.setAuthor(UserConverterUtil.toEntity(dto.getAuthor()));
        Set<RaceTypeDTO> raceTypes = dto.getRaceTypes();
        Set<RaceTypeEntity> raceTypesRaceTypeEntities = new HashSet<>();

        if (raceTypes != null) {
            for (RaceTypeDTO raceTypeDTO : raceTypes) {
                RaceTypeEntity raceTypeEntity = toEntity(raceTypeDTO);
                raceTypesRaceTypeEntities.add(raceTypeEntity);
            }
        }
        raceEntity.setRaceTypeEntities(raceTypesRaceTypeEntities);

        return raceEntity;
    }

    /**
     * Convert a MyRaceDTO to an Entity
     * @param dto A MyRace DTO
     * @return MyRaceEntity ac cnverted Entity
     */
    public static MyRaceEntity toEntity(MyRaceDTO dto){

        MyRaceEntity entity = new MyRaceEntity(dto.getUserDTO().getId(), dto.getRaceDTO().getId());
        entity.setCost(dto.getCost());
        entity.setHotelName(dto.getHotelName());

        if (dto.getModifiedDate() == null){
            entity.setModifiedDate(LocalDateTime.now());
        } else {
            entity.setModifiedDate(dto.getModifiedDate());
        }
        entity.setMyRaceStatus(dto.getMyRaceStatus());
        entity.setNotes(dto.getNotes());
        entity.setPaid(dto.isPaid());
        if (dto.getRegistrationDate() != null){
            entity.setRegistrationDate(dto.getRegistrationDate().toLocalDate());
        } else {
            entity.setRegistrationDate(LocalDate.now());
        }
        if (dto.getRaceTypes() != null) {
            Set<RaceTypeEntity> raceTypeSet = new HashSet<>(dto.getRaceTypes().size());
            dto.getRaceTypes().forEach(
                    raceTypeDTO -> {
                        RaceTypeEntity raceTypeEntity = RaceTypeEntity.builder()
                                .id(raceTypeDTO.getId())
                                .name(raceTypeDTO.getName())
                                .shortDesc(raceTypeDTO.getShortDesc())
                                .description(raceTypeDTO.getDesc()).build();
                        raceTypeSet.add(raceTypeEntity);
                    }
            );

            entity.setRaceTypeEntities(raceTypeSet);
        }
        return entity;
    }

    /**
     * Convert from a set of RaceType Entities to a set of RaceType DTO assuming
     * the list is not null. If null then null is returned and not an empty list
     *
     * @param entities Set of RaceTypeEntity objects
     * @return Set<RaceTypeDTO> a converted list
     */
    public static Set<RaceTypeDTO> toDTO(Set<RaceTypeEntity> entities) {
        Set<RaceTypeDTO> raceTypeDTOSet = null;

        if (entities != null){
            raceTypeDTOSet = new HashSet<>(entities.size());
            for (RaceTypeEntity raceTypeEntity : entities) {
                raceTypeDTOSet.add(RaceConverterUtil.toDTO(raceTypeEntity));
            }
        }

        return raceTypeDTOSet;
    }
    /**
     * Convert from Race Type Entity to a DTO
     * @param raceTypeEntity Race Entity
     * @return RaceTypeDTO a converted DTO
     */
    public static RaceTypeDTO toDTO(RaceTypeEntity raceTypeEntity) {
        return RaceTypeDTO.builder()
                .id(raceTypeEntity.getId())
                .name(raceTypeEntity.getName())
                .desc(raceTypeEntity.getDescription())
                .shortDesc(raceTypeEntity.getShortDesc())
                .build();
    }

    /**
     * Convert from RaceType DTO to Entity
     * @param dto The Race Type Entity
     * @return RaceTypeEntity a converted Entity
     */
    private static RaceTypeEntity toEntity(RaceTypeDTO dto) {

        RaceTypeEntity entity = new RaceTypeEntity();
        entity.setId(dto.getId());
        entity.setDescription(dto.getDesc());
        entity.setShortDesc(dto.getShortDesc());
        entity.setName(dto.getName());
        return entity;
    }


    /**
     * Convert from Address Entity to a DTO
     * @param entity Address Entity
     * @return AddressDTO a converted DTO
     */
    public static AddressDTO toDTO(AddressEntity entity) {
        return AddressDTO.builder()
                                .id(entity.getId())
                                .location(entity.getLocation())
                                .street(entity.getStreet())
                                .city(entity.getCity())
                                .zip(entity.getZip())
                                .state(entity.getState())
                                .country(entity.getCountry())
                                .notes(entity.getNotes())
                                .phone(entity.getPhone())
                                .authorId(entity.getAuthorId())
                                .modDate(entity.getModDate())
                                .build();
    }

    /**
     * Convert a valid dto to an entity. Entity will be null if
     * dto is null. No exception will be thrown.
     *
     * @param dto Address DTO to convert
     * @return AddressEntity will be converted from DTO assuming it is not null
     */
    public static AddressEntity toEntity(AddressDTO dto) {
        AddressEntity entity = null;

        if (dto != null) {
            entity = new AddressEntity();
            entity.setId(dto.getId());
            entity.setLocation(dto.getLocation());
            entity.setStreet(dto.getStreet());
            entity.setCity(dto.getCity());
            entity.setState(dto.getState());
            entity.setZip(dto.getZip());
            entity.setCountry(dto.getCountry());
            entity.setNotes(dto.getNotes());
            entity.setPhone(dto.getPhone());
            entity.setModDate(dto.getModDate());
            if (dto.getAuthorId() == null){
                entity.setAuthorId(DEFAULT_AUTHOR_ID);
            } else {
                entity.setAuthorId(dto.getAuthorId());
            }
        }
        return entity;
    }

    /**
     * Convert a Iterable<RaceTypeEntity> into a List of RaceTypeDTO
     * @param raceTypeEntities The list of RaceTypeEntity to convert
     * @return List<RaceTypeDTO> a list of converted RaceTypeEntity objects
     */
    public static List<RaceTypeDTO> toDTO(Iterable<RaceTypeEntity> raceTypeEntities) {
        List<RaceTypeDTO> raceTypeDTOList = new ArrayList<>();
        raceTypeEntities.forEach(
                raceTypeEntity -> raceTypeDTOList.add(toDTO(raceTypeEntity))
        );
        return raceTypeDTOList;
    }
}
