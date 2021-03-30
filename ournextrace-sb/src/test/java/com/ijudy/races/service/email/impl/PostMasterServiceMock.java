package com.ijudy.races.service.email.impl;

import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.ijudy.races.pojo.ContactUs;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.service.email.PostMasterService;

public class PostMasterServiceMock implements PostMasterService {


    @Override
    public SendEmailResult sendResetEmail(UserDTO user) {
        return null;
    }

    @Override
    public SendEmailResult sendContactUsEmail(ContactUs contactUsDetails) {return null;}

}

