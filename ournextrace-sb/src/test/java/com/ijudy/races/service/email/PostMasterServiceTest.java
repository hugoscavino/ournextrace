package com.ijudy.races.service.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.ijudy.races.dto.PasswordResetTokenDTO;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.pojo.ContactUs;
import com.ijudy.races.service.email.impl.PostMasterServiceImpl;
import com.ijudy.races.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PostMasterServiceTest {

    @Configuration
    static class Config {
        @Bean
        PostMasterService PostMasterService() {
            return new PostMasterServiceImpl();
        }
    }

    @MockBean
    private UserService userService;

    @MockBean
    private AmazonSimpleEmailService amazonSimpleEmailService;

    @Autowired
    private PostMasterService postMasterService;

    @Test
    void sendResetEmail() {
        Long userId = 1L;
        PasswordResetTokenDTO token = PasswordResetTokenDTO.builder().token("123").expiryDate(LocalDateTime.now()).build();
        when(userService.createPasswordResetTokenForUser(userId)).thenReturn(token);

        final String msgId = "123";
        SendEmailResult sendEmailResult = new SendEmailResult();
        sendEmailResult.setMessageId(msgId);
        when(amazonSimpleEmailService.sendEmail(any())).thenReturn(sendEmailResult);

        UserDTO user = UserDTO.builder().id(1L).email("test@aol.com").firstName("test").lastName("lastname").build();
        SendEmailResult result = postMasterService.sendResetEmail(user);
        assertThat(result).isNotNull();
        assertThat(result.getMessageId()).isNotNull();
        assertThat(result.getMessageId()).isEqualTo(msgId);
    }

    @Test
    void sendContactUsEmail() {
        Long userId = 1L;
        PasswordResetTokenDTO token = PasswordResetTokenDTO.builder().token("123").expiryDate(LocalDateTime.now()).build();
        when(userService.createPasswordResetTokenForUser(userId)).thenReturn(token);

        final String msgId = "123";
        SendEmailResult sendEmailResult = new SendEmailResult();
        sendEmailResult.setMessageId(msgId);
        when(amazonSimpleEmailService.sendEmail(any())).thenReturn(sendEmailResult);

        final String email = "test@aol.com";
        final String msg = "some message";
        ContactUs contactUs = new ContactUs();
        contactUs.setCaptchaResponse("captcha");
        contactUs.setEmail(email);
        contactUs.setMessage(msg);

        SendEmailResult result = postMasterService.sendContactUsEmail(contactUs);
        assertThat(result).isNotNull();
        assertThat(result.getMessageId()).isNotNull();
        assertThat(result.getMessageId()).isEqualTo(msgId);
    }
}
