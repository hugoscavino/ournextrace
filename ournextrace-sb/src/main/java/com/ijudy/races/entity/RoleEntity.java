package com.ijudy.races.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role", schema = DatabaseMetadata.SCHEMA_NAME)
@Data
public class RoleEntity {

    @Id
    private Integer id;

    @EqualsAndHashCode.Exclude
    private String name;

    @EqualsAndHashCode.Exclude
    private String description;

}
