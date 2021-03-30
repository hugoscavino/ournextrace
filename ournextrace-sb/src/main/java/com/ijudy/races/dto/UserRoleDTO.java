package com.ijudy.races.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRoleDTO {

    private int userId;
    private int roleId;

    @EqualsAndHashCode.Exclude
    private String  roleName;

    @EqualsAndHashCode.Exclude
    private String  roleDesc;

}
