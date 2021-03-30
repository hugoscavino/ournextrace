package com.ijudy.races.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PasswordResetTokenDTO {

        private Long     	    id;
        private Long 	        userId;
        private String 		    token;
        private LocalDateTime   expiryDate;

        /**
         * Constructor for PasswordResetToken
         * @param id			Auto Generated ID
         * @param userId		user Id of the UserEntity
         * @param token			Generated Token
         * @param expiryDate	Expiration Date
         */
        public PasswordResetTokenDTO(Long id, Long userId, String token, LocalDateTime expiryDate) {
            this.id  		= id;
            this.userId 	= userId;
            this.token  	= token;
            this.expiryDate = expiryDate;
        }

        /**
         * Constructor for PasswordResetToken
         *
         * @param userId		userId of the UserEntity
         * @param token			Generated Token
         * @param expiryDate	Expiration Date
         */
        public PasswordResetTokenDTO(Long userId, String token, LocalDateTime expiryDate) {
            this.userId 	= userId;
            this.token  	= token;
            this.expiryDate = expiryDate;
        }
    }
