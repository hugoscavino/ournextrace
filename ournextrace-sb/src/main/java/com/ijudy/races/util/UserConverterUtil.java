package com.ijudy.races.util;

import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.entity.RoleEntity;
import com.ijudy.races.entity.UserEntity;
import com.ijudy.races.enums.RoleNames;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public final class UserConverterUtil {


    /**
     * Convert the entity to a DTO with the password in clear text
     *
     * @param entity JPA Entiry to convert
     *
     * @return UserDTO converted from enity
     */
    public static UserDTO toDTO(UserEntity entity) {

        UserDTO dto = UserDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getCred())
                .active(entity.getActive())
                .name(entity.getName())
                .city(entity.getCity())
                .state(entity.getState())
                .country(entity.getCountry())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .zip(entity.getZip())
                .socialProvider(entity.getSocialProvider())
                .lastLogin(entity.getLastLogin())
                .lastUpdated(entity.getLastUpdated())
                .build();

                dto.setUser(UserDTO.isUser(entity.getRoleEntities()));
                dto.setPowerUser(UserDTO.isPowerUser(entity.getRoleEntities()));
                dto.setAdmin(UserDTO.isAdmin(entity.getRoleEntities()));

        return dto;
    }

    public static UserEntity toEntity( UserDTO dto){

        UserEntity.UserEntityBuilder entityBuilder = UserEntity.builder()
                .id(dto.getId())
                .cred(dto.getPassword())
                .email(dto.getEmail())
                .active(dto.isActive())
                .name(dto.getName())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .zip(dto.getZip())
                .socialProvider(dto.getSocialProvider())
                .lastLogin(dto.getLastLogin())
                .lastUpdated(dto.getLastUpdated());

        Set<RoleEntity> set = new HashSet<>(3);

        if (dto.isUser()){
            RoleEntity userRoleEntity = new RoleEntity();
            userRoleEntity.setId(RoleNames.USER.toId());
            set.add(userRoleEntity);
        }

        if (dto.isPowerUser()){
            RoleEntity userRoleEntity = new RoleEntity();
            userRoleEntity.setId(RoleNames.POWER_USER.toId());
            set.add(userRoleEntity);
        }

        if (dto.isAdmin()){
            RoleEntity userRoleEntity = new RoleEntity();
            userRoleEntity.setId(RoleNames.ADMIN.toId());
            set.add(userRoleEntity);
        }
        entityBuilder.roleEntities(set);

        return entityBuilder.build();
    }
    
}
