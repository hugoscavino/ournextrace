package com.ijudy.races.dto;

import com.ijudy.races.enums.RoleNames;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleDTO {

    public final static RoleDTO USER        = RoleDTO.builder().id(1).name(RoleNames.USER.toString()).build();
    public final static RoleDTO POWER_USER  = RoleDTO.builder().id(2).name(RoleNames.POWER_USER.toString()).build();
    public final static RoleDTO ADMIN       = RoleDTO.builder().id(3).name(RoleNames.ADMIN.toString()).build();

    private int id;

    @EqualsAndHashCode.Exclude
    private String name;

    @EqualsAndHashCode.Exclude
    private String desc;

}
