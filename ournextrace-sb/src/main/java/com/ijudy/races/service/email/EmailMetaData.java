package com.ijudy.races.service.email;

import com.ijudy.races.dto.UserDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailMetaData {

    private UserDTO user;
    private String subject;
    private String toEmail;
    private String emailFrom;

}
