package com.ijudy.races.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.ankurpathak.password.bean.constraints.ContainDigit;
import com.github.ankurpathak.password.bean.constraints.ContainSpecial;
import com.github.ankurpathak.password.bean.constraints.NotContainWhitespace;
import com.github.ankurpathak.password.bean.constraints.PasswordMatches;
import com.ijudy.races.entity.RoleEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@PasswordMatches
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDTO {


	private Long id;

	@NotContainWhitespace
	private String 	email;

	@NotNull
	@Size(min=8, max=14)
    @NotContainWhitespace
    @ContainSpecial
    @ContainDigit
    private String 	password;

	@NotBlank
	@NotContainWhitespace
	@EqualsAndHashCode.Exclude
	private String 	confirmPassword;

	private String 	name;

	private boolean active;

	@NotContainWhitespace
	private String 	city;

	@NotContainWhitespace
	private String 	state;

	@NotContainWhitespace
	private String 	country;

	@NotContainWhitespace
	private String 	firstName;

	@NotContainWhitespace
	private String 	lastName;

	@NotContainWhitespace
	private String 	zip;

	@NotContainWhitespace
	private String 	socialProvider;

	private boolean isUser;

	private boolean isPowerUser;

	private boolean isAdmin;

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@EqualsAndHashCode.Exclude
	private LocalDateTime lastUpdated;

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@EqualsAndHashCode.Exclude
	private LocalDateTime lastLogin;

	/**
	 * Used only for password reset. Not stored in database
	 */
	@EqualsAndHashCode.Exclude
	private String 	token;

	static public Boolean isAdmin(Set<RoleEntity> entities){
		return isSomeUser(entities, RoleDTO.ADMIN);
	}

	static public Boolean isPowerUser(Set<RoleEntity> entities){
		return isSomeUser(entities, RoleDTO.POWER_USER);
	}
	static public Boolean isUser(Set<RoleEntity> entities){
		return isSomeUser(entities, RoleDTO.USER);
	}

	static private Boolean isSomeUser(Set<RoleEntity> entities, RoleDTO dto){
		var result = new AtomicBoolean(false);
		entities.forEach(
				roleEntity -> {
					int roleId = roleEntity.getId();
					if (roleId == dto.getId()){
						result.set(true);
					}
				}
		);
		return result.get();
	}
}
