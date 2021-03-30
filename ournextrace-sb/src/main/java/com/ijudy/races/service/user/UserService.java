package com.ijudy.races.service.user;

import com.ijudy.races.dto.PasswordResetTokenDTO;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.security.SocialUserKey;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Operations for anonymous and registered users
 *
 */
public interface UserService {

    Optional<UserDTO> findByPrincipal(Principal principal);

    /**
     * Return the UserDTO
     * @param userId The User's ID
     * @return UserDTO a secure of sanitized UserDTO object
     */
    Optional<UserDTO> findById(Long userId);

    /**
     * Return user if there is one with this email and social provider
     * @param email Will not be validated
     * @param socialProvider has to be ijudy, google or facebook
     * @return Optional<UserDTO>
     */
    Optional<UserDTO> findByEmailAndSocialProvider(String email, String socialProvider);
    Optional<UserDTO> findByEmailAndSocialProvider(String email);
    Optional<UserDTO> findByEmailAndSocialProvider(SocialUserKey key);
    /**
     * Register the user in the repository
     * @param user The required fields for the user
     * @return UserDTO The newly registered user with an ID
     */
    UserDTO registerLocalUser(UserDTO user);

    /**
     * Register the user in the repository from a social provider account
     * @param user The required fields for the user
     * @return UserDTO The newly registered user with an ID and no password
     */
    UserDTO registerSocialUser(UserDTO user);

    /**
     * Reset the password for the user
     * @param user The user to update
     * @return UserDTO The user whose password was updated
     */
    UserDTO resetPassword(UserDTO user);

    /**
     * For the reset password use case save a
     * row in the forgot password repository
     * so when later the user clicks on the
     * URL the password will be rest
     * @param userId the user's ID
     * @return PasswordResetTokenEntity the entity
     */
    PasswordResetTokenDTO createPasswordResetTokenForUser(Long userId);


    /**
     * For password reset find the user with the
     * associated token given an user id
     * @param userId The user's ID
     * @param token The generated token when the password reset was requested
     * @return UserDTO if found will be populated, null otherwise
     */
    PasswordResetTokenDTO findByUserIdAndToken(Long userId, String token);

    /**
     * For password reset find the user with the
     * associated token given an email
     * @param email The user's email
     * @param token The generated token when the password reset was requested
     * @return UserDTO if found will be populated, null otherwise
     */
    Optional<UserDTO> findUserWithToken(String email, String token);

    /**
     * Update the user's profile. Do not update
     * PK fields and passwords
     * @param user The user to update
     * @return UserDTO The updated user
     */
    Optional<UserDTO> updateProfile(UserDTO user);

    /**
     * Find all the registered users
     *
     * @return List<UserDTO> list of users
     */
    List<UserDTO> findAllUsers();

    List<UserDTO> findAllUsersByEmail(String email);

}
