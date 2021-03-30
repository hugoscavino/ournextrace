package com.ijudy.races.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "forgot_password", schema = DatabaseMetadata.SCHEMA_NAME)
@Data
@NoArgsConstructor
public class PasswordResetTokenEntity {

    @Id()
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long     	    id;

    private Long 	        userId;

    private String 		    token;

    private LocalDateTime   expiryDate;

    /**
     * Create a PasswordResetTokenEntity with all the arguments
     * minus the Auto Generated Key
     *
     * @param userId
     * @param token
     * @param expiryDate
     */
    public PasswordResetTokenEntity(Long userId, String token, LocalDateTime expiryDate) {
        this.userId = userId;
        this.token = token;
        this.expiryDate = expiryDate;
    }

}
