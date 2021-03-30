package com.ijudy.races.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Environment implements Serializable {

    private String env;

    public Environment(final String value){
        env = value;
    }

}
