package com.ijudy.races.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id()
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String cred;
    private String email;

    private Boolean active;

    private String name;
    private String city;
    private String state;
    private String country;
    private String firstName;
    private String lastName;
    private String zip;

    /**
     * What if any social provider was used to authenticate
     */
    private String socialProvider;

    /**
     * Date of last successful login
     */
    private LocalDateTime lastLogin;

    private LocalDateTime lastUpdated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_role",
            joinColumns         = @JoinColumn(name = "user_id"),
            inverseJoinColumns  = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roleEntities;

}
