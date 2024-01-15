package com.ijudy.races.pojo;

import lombok.Data;

@Data
public class Environment {

    private String env;

    public Environment(final String value){
        env = value;
    }

}
