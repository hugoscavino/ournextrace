package com.ijudy.races.service.user.impl;

import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.entity.UserEntity;
import com.ijudy.races.repository.UserRepository;
import com.ijudy.races.service.user.UserService;
import com.ijudy.races.util.UserConverterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public List<UserDTO> findAllUsers() {

        List<UserDTO> list = new ArrayList<>();
        for (UserEntity userEntity : userRepository.findAll()) {
            UserDTO userDTO = UserConverterUtil.toDTO(userEntity);
            userDTO.setPassword("");
            list.add(userDTO);
        }
        return list;
    }


    /**
     * Return the User without the passwords and tokens
     *
     * @param userId the user individual id
     * @return UserDTO sanitized
     */
    public Optional<UserDTO> findById(Long userId){

        return userRepository.findById(userId).map(
                UserConverterUtil::toDTO
        );
    }



}
