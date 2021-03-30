package com.ijudy.races.service.security;

import com.ijudy.races.dto.RoleDTO;
import com.ijudy.races.enums.RoleNames;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Data
public class CustomGrantedAuthority implements GrantedAuthority {

    public static final CustomGrantedAuthority USER = new CustomGrantedAuthority(RoleNames.USER.toString());
    public static final CustomGrantedAuthority POWER_USER = new CustomGrantedAuthority(RoleNames.POWER_USER.toString());
    public static final CustomGrantedAuthority ADMIN = new CustomGrantedAuthority(RoleNames.ADMIN.toString());

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String roleName;

    public CustomGrantedAuthority(String roleName) {
        this.roleName = roleName;
    }

    public CustomGrantedAuthority(RoleDTO role) {
        this.roleName = role.getName();
    }

    @Override
    public String getAuthority() {
        return roleName;
    }

}
