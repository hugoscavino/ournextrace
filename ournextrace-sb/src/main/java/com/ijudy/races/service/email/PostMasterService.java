package com.ijudy.races.service.email;

import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.ijudy.races.pojo.ContactUs;
import com.ijudy.races.dto.UserDTO;


public interface PostMasterService {

    /**
     * Send the email using the AWS service
     *
     * @param user
     * @return SendEmailResult from the email service
     */
    SendEmailResult sendResetEmail(UserDTO user);

    /**
     * Send the contact us email using the AWS service
     *
     * @param contactUsDetails
     * @return SendEmailResult from the email service
     */
    SendEmailResult sendContactUsEmail(ContactUs contactUsDetails);

}
