package com.ijudy.races.service.security;

import com.ijudy.races.dto.RoleDTO;
import com.ijudy.races.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class UserPrincipal implements OAuth2User, UserDetails {

    private UserDTO userDTO;
    private Map<String, Object> attributes;

    public UserPrincipal(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public UserPrincipal(UserDTO userDTO, Map<String, Object> attributes) {
        this.userDTO = userDTO;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<CustomGrantedAuthority> authorities = new HashSet<>(3);
        if (userDTO.isUser()){
            authorities.add(new CustomGrantedAuthority(RoleDTO.USER));
        }
        if (userDTO.isPowerUser()){
            authorities.add(new CustomGrantedAuthority(RoleDTO.POWER_USER));
        }
        if (userDTO.isAdmin()){
            authorities.add(new CustomGrantedAuthority(RoleDTO.ADMIN));
        }
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getPassword() {
        return userDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return userDTO.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userDTO.isUser();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userDTO.isUser();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userDTO.isUser();
    }

    @Override
    public boolean isEnabled() {
        return userDTO.isUser();
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }

    public String getSocialProvider() {
        return userDTO.getSocialProvider();
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }
}
