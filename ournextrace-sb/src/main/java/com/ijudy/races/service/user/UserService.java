package com.ijudy.races.service.user;

import com.ijudy.races.dto.UserDTO;

import java.util.List;
import java.util.Optional;

/**
 * Operations for anonymous and registered users
 *
 */
public interface UserService {

    /**
     * Return the UserDTO
     * @param userId The User's ID
     * @return UserDTO a secure of sanitized UserDTO object
     */
    Optional<UserDTO> findById(Long userId);

    /**
     * Find all the registered users
     *
     * @return List<UserDTO> list of users
     */
    List<UserDTO> findAllUsers();


}
