package com.ijudy.races.respository;

import com.ijudy.races.entity.RoleEntity;
import com.ijudy.races.entity.UserEntity;
import com.ijudy.races.RoleNames;
import com.ijudy.races.SocialProvider;
import com.ijudy.races.repository.RoleRepository;
import com.ijudy.races.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("UserEntity Repo Test")
@Slf4j
class UserEntityRepositoryTest {

    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Test if find all returns all the values")
    void getAllTest() {
        UserEntity user1 = UserEntity.builder().email("test1@aol.com").active(true).name("Name1").build();
        UserEntity user2 = UserEntity.builder().email("test2@aol.com").active(true).name("Name2").build();
        repo.save(user1);
        repo.save(user2);

        Iterable<UserEntity> iterator = repo.findAll();
        assertNotNull(iterator);

        assertTrue(iterator.iterator().hasNext());
    }

    @Test
    @DisplayName("Test Finding One UserEntity by Id")
    void getById()  {

        UserEntity user3 = UserEntity.builder().email("test3@aol.com").active(true).name("Name3").build();
        UserEntity savedUser = repo.save(user3);
        log.info(savedUser.toString());

        Optional<UserEntity> foundUser = repo.findById( savedUser.getId());
        assertNotNull(foundUser);
        assertFalse(foundUser.isEmpty());

    }

    @Test
    @DisplayName("Test Finding One UserEntity by Id")
    void getByEmail()  {

        String email = "test4@aol.com";
        UserEntity user4 = UserEntity.builder().email(email).active(true).name("Name4").build();
        UserEntity savedUser = repo.save(user4);

        Optional<UserEntity> foundUser = repo.findByEmailAndSocialProvider(email, SocialProvider.ijudy.toString());
        assertNotNull(foundUser.get());
        assertTrue(foundUser.get().equals(user4));

    }

    @Test
    @DisplayName("Test Roles")
    void getRoles()  {

        String email = "test5@aol.com";
        UserEntity user5 = UserEntity.builder().active(true).email(email).name("Hugo Scavino").firstName("hugo").lastName("scavino").build();
        Optional<RoleEntity> userRole = roleRepository.findById(RoleNames.USER.toId());

        userRole.ifPresent(
                roleEntity -> {
                   Set<RoleEntity> roleEnities = of(userRole.get()).collect(Collectors.toCollection(HashSet::new));
                   user5.setRoleEntities(roleEnities);
                }
        );

        repo.save(user5);
        Optional<UserEntity> foundUser = repo.findByEmailAndSocialProvider( email, SocialProvider.ijudy.toString());
        assertNotNull(foundUser);
        assertEquals(1, foundUser.get().getRoleEntities().size());

        RoleEntity oneRoleEntity = (RoleEntity) foundUser.get().getRoleEntities().toArray()[0];
        assertEquals((int) oneRoleEntity.getId(), RoleNames.USER.toId());

    }

    @Test
    @DisplayName("Test Roles")
    void getMultipleRoles()  {

        String email = "test6@aol.com";
        UserEntity user6 = UserEntity.builder().active(true).email(email).name("Hugo Scavino").firstName("hugo").lastName("scavino").build();
        Optional<RoleEntity> userRole = roleRepository.findById(RoleNames.USER.toId());
        Optional<RoleEntity> adinRole = roleRepository.findById(RoleNames.ADMIN.toId());

        Set<RoleEntity> roleEnities = new HashSet<>();
        roleEnities.add(userRole.get());
        roleEnities.add(adinRole.get());
        user6.setRoleEntities(roleEnities);

        repo.save(user6);
        Optional<UserEntity> foundUser = repo.findByEmailAndSocialProvider(email, SocialProvider.ijudy.toString());
        assertNotNull(foundUser);
        assertEquals(2, foundUser.get().getRoleEntities().size());

        RoleEntity oneRoleEntity = (RoleEntity) foundUser.get().getRoleEntities().toArray()[0];
        assertTrue(oneRoleEntity.getId().intValue() == RoleNames.USER.toId(), "Expected User to have USER ROLE");

        RoleEntity secondRoleEntity = (RoleEntity) foundUser.get().getRoleEntities().toArray()[1];
        assertTrue(secondRoleEntity.getId().intValue() == RoleNames.ADMIN.toId(), "Expected User to have ADMIN ROLE");
    }
}
