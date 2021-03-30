package com.ijudy.races.controller;

import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.ijudy.races.service.captcha.GenericResponse;
import com.ijudy.races.service.captcha.ICaptchaService;
import com.ijudy.races.pojo.ContactUs;
import com.ijudy.races.exception.FailedCaptcha;
import com.ijudy.races.exception.ReCaptchaInvalidException;
import com.ijudy.races.service.email.PostMasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value="/api/v2")
@Slf4j
public class ContactUsController {

    private final ICaptchaService captchaService;
    private final PostMasterService postMasterService;

    public ContactUsController(ICaptchaService captchaService, PostMasterService postMasterService) {
        this.captchaService = captchaService;
        this.postMasterService = postMasterService;
    }

    @PostMapping(value = "/contact")
    @ResponseBody
    public GenericResponse contactUs(@RequestBody ContactUs contactUs, HttpServletRequest request) {
        String response = contactUs.getCaptchaResponse();
        log.debug("Attempting to verify captcha");
        GenericResponse gen = new GenericResponse("success");
        try {
            captchaService.processResponse(response);
            log.debug("UserEntity " + contactUs.getEmail() + " is contacting us : captcha = OK");
            final SendEmailResult sendEmailResult = postMasterService.sendContactUsEmail(contactUs);
            log.info(contactUs.getEmail() + " sent us an email with message Id" + sendEmailResult.getMessageId());
            // TODO create an SMS message so we can monitor
        } catch (ReCaptchaInvalidException e) {
            String msg ="UserEntity " + contactUs.getEmail() + " failed captcha";
            log.warn(msg);
            throw new FailedCaptcha(msg);
        }

        return gen;
    }
}
