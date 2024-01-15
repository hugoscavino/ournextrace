package com.ijudy.races;

public enum RoleNames {
    /**
     * Authenticated User
     */
    USER(1),

    /**
     * User with additional privileges
     * to manage races and meta data
     */
    POWER_USER(2),

    /**
     * Able to make system changes
     */
    ADMIN(3);

    private int roleId;

    /**
     * Int value of the Role
     *
     * @return int value of the Role
     */
    public int toId(){
        return roleId;
    }

    @Override
    public String toString(){
        String name = "USER";
        if (roleId == 2){
            name = "POWER_USER";
        }
        if (roleId == 3){
            name = "ADMIN";
        }
        return name;
    }

    RoleNames(int roleId){
        this.roleId = roleId;
    }

}
