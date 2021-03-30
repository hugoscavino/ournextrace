package com.ijudy.races.service.user.impl;

import com.ijudy.races.dto.PasswordResetTokenDTO;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.entity.PasswordResetTokenEntity;
import com.ijudy.races.entity.UserEntity;
import com.ijudy.races.enums.SocialProvider;
import com.ijudy.races.exception.NotFoundException;
import com.ijudy.races.repository.PasswordResetTokenRepository;
import com.ijudy.races.repository.UserRepository;
import com.ijudy.races.security.SocialUserKey;
import com.ijudy.races.security.UserUtils;
import com.ijudy.races.service.security.CustomUserDetailsService;
import com.ijudy.races.service.user.UserService;
import com.ijudy.races.util.UserConverterUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Value(value = "${email.password.reset.hours}")
    private int resetHours;

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public UserDTO registerLocalUser(UserDTO user) {
        UserDTO saveUser = saveLocalUserFirstTime(user);
        return processSecurityContext(saveUser);
    }

    @Override
    public UserDTO registerSocialUser(UserDTO user) {
        UserDTO saveUser = saveSocialUserFirstTime(user);
        return processSecurityContext(saveUser);
    }

    private UserDTO processSecurityContext(UserDTO dto){
        final Optional<UserEntity> userEntity = userRepository.findByEmailAndSocialProvider(dto.getEmail(), dto.getSocialProvider());
        if (!userEntity.isEmpty()){
            return UserConverterUtil.toDTO(userEntity.get());
        } else
            return null;
    }

    @Override
    public UserDTO resetPassword(UserDTO user) {
        Optional<UserDTO> foundUser = findUserWithToken(user.getEmail(), user.getToken());
        if (foundUser.isEmpty()) {
            throw new NotFoundException(user.getEmail());
        } else {
            foundUser.get().setPassword(user.getPassword());
            return updateUserPassword(foundUser.get());
        }
    }

    private UserDTO updateUserPassword(UserDTO userDTO) {

        String unencryptedCred = userDTO.getPassword();
        userDTO.setLastUpdated(LocalDateTime.now());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userDTO.setPassword(bCryptPasswordEncoder.encode(unencryptedCred));

        UserEntity userEntity = UserConverterUtil.toEntity(userDTO);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return UserConverterUtil.toDTO(savedUserEntity);
    }

    @Override
    public PasswordResetTokenDTO createPasswordResetTokenForUser(Long userId) {
        String token = UUID.randomUUID().toString();
        LocalDateTime later = LocalDateTime.now().plusHours(resetHours);
        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity(userId, token, later);
        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.save(resetToken);
        return UserConverterUtil.toDTO(passwordResetTokenEntity);
    }

    @Override
    public PasswordResetTokenDTO findByUserIdAndToken(Long userId, String token) {
        final PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByUserIdAndToken(userId, token);
        if (passwordResetTokenEntity != null){
            return UserConverterUtil.toDTO(passwordResetTokenEntity);
        } else {
            log.warn("Could not findByUserIdAndToken for ID " + userId);
            return null;
        }
    }

    @Override
    public Optional<UserDTO> findUserWithToken(String email, String token) {

        final Optional<UserEntity> userEntity = userRepository.findByEmailAndSocialProvider(email, SocialProvider.ijudy.toString());
        if (userEntity.isPresent()) {
            Long id = userEntity.get().getId();
            final PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByUserIdAndToken(id, token);
            if (passwordResetTokenEntity != null) {
                return Optional.of(UserConverterUtil.toDTO(userEntity.get()));
            } else {
                log.warn("Could not findUserWithToken for " + email);
                return Optional.empty();
            }
        } else {
            log.warn("Could not findByEmail in findUserWithToken for " + email);
            return Optional.empty();
        }

    }

    @Override
    public Optional<UserDTO> updateProfile(UserDTO user) {
        final Optional<UserEntity> foundEntity = userRepository.findByEmailAndSocialProvider(user.getEmail(), user.getSocialProvider());
        if (foundEntity.isEmpty()) {
            log.warn("Updating profile with key - email [" + user.getEmail() + "], social provider [" + user.getSocialProvider() + "]");
            return Optional.empty();
        } else {
            UserEntity realEntity = foundEntity.get();
            realEntity.setLastUpdated(LocalDateTime.now());

            // Only allow to save a sub set of attributes
            realEntity.setCity(user.getCity());
            realEntity.setState(user.getState());
            realEntity.setCountry(user.getCountry());
            realEntity.setZip(user.getZip());
            realEntity.setFirstName(user.getFirstName());
            realEntity.setLastName(user.getLastName());
            UserEntity savedUserEntity = userRepository.save(realEntity);
            final UserDTO userDTO = UserConverterUtil.toDTO(savedUserEntity);
            return Optional.of(userDTO);
        }
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

    @Override
    public List<UserDTO> findAllUsersByEmail(String email) {
        List<UserDTO> list = new ArrayList<>();
        for (UserEntity userEntity : userRepository.findByEmail(email)) {
            UserDTO userDTO = UserConverterUtil.toDTO(userEntity);
            userDTO.setPassword("");
            list.add(userDTO);
        }
        return list;
    }

    @Override
    public Optional<UserDTO> findByEmailAndSocialProvider(String email, String socialProvider) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(socialProvider)){
            return Optional.empty();
        }
        String lowerCasedEmail = email.toLowerCase();
        final Optional<UserEntity> optionalUserEntity = userRepository.findByEmailAndSocialProvider(lowerCasedEmail, socialProvider);
        return optionalUserEntity.map(UserConverterUtil::toDTO);
    }

    @Override
    public Optional<UserDTO> findByEmailAndSocialProvider(String email) {
        return findByEmailAndSocialProvider(email, SocialProvider.ijudy.toString());
    }

    @Override
    public Optional<UserDTO> findByEmailAndSocialProvider(SocialUserKey key) {
        return findByEmailAndSocialProvider(key.email, key.socialProvider);
    }

    @Override
    public Optional<UserDTO> findByPrincipal(Principal principal) {
        final SocialUserKey socialUserKey = UserUtils.getSocialUserKey(principal);
        return findByEmailAndSocialProvider(socialUserKey);
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

    /**
     * Save user for the first time in the repo
     * @param userDTO The UserDTO to save
     * @return Optional<UserDTO> The Saved User
     */
    private UserDTO saveLocalUserFirstTime(UserDTO userDTO) {
        userDTO.setLastUpdated(LocalDateTime.now());
        userDTO.setActive(true);
        userDTO.setUser(true);
        userDTO.setPowerUser(false);
        userDTO.setAdmin(false);
        userDTO.setSocialProvider(SocialProvider.ijudy.toString());
        String unencryptedCred = userDTO.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userDTO.setPassword(bCryptPasswordEncoder.encode(unencryptedCred));

        UserEntity userEntity = UserConverterUtil.toEntity(userDTO);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        userDTO.setId(savedUserEntity.getId());
        return userDTO;
    }

    /**
     * Save user for the first time in the repo using no
     * password as they are being authenticated by a
     * social provider
     * @param userDTO The UserDTO to save
     * @return Optional<UserDTO> The Saved User
     */
    private UserDTO saveSocialUserFirstTime(UserDTO userDTO) {
        userDTO.setLastUpdated(LocalDateTime.now());
        userDTO.setActive(true);
        userDTO.setUser(true);
        userDTO.setPowerUser(false);
        userDTO.setAdmin(false);
        // Leave password alone in case they want to keep using their local logon
        // userDTO.setPassword("SOCIAL_AUTH");
        UserEntity userEntity = UserConverterUtil.toEntity(userDTO);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        userDTO.setId(savedUserEntity.getId());
        return userDTO;
    }


}
