package com.ijudy.races.service.email.impl;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.ijudy.races.dto.PasswordResetTokenDTO;
import com.ijudy.races.dto.UserDTO;
import com.ijudy.races.pojo.ContactUs;
import com.ijudy.races.service.email.PostMasterService;
import com.ijudy.races.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class PostMasterServiceImpl implements PostMasterService {

	private static final String EMAIL_ENCODING = "UTF-8";

	@Autowired
	private UserService userService;

	@Autowired
	private AmazonSimpleEmailService amazonSimpleEmailService;

	// Contact Us
	@Value( "${email.html.template.contact}" )
	private String emailHtmlTemplateContact;

	@Value( "${email.txt.template.contact}" )
	private String emailTextTemplateContact;

	@Value( "${email.subject.contact}" )
	private String emailSubjectContact;

	@Value( "${email.admin.to}" )
	private String emailAdminTo;

	@Value( "${email.from.contact}" )
	private String emailFromContact;

	// Reset Password
	@Value( "${email.html.template.reset}" )
	private String emailHtmlTemplateReset;

	@Value( "${email.txt.template.reset}" )
	private String emailTextTemplateReset;

	@Value( "${email.subject.reset}" )
	private String emailSubjectReset;

	@Value( "${email.from.reset}" )
	private String emailFromReset;

	@Value("${email.api-server.url}")
	private String emailApiServerUrl;
	
	@Value( "${email.password.reset.hours:4}" )
	private String passwordResetHours;


	public PostMasterServiceImpl() {
		Velocity.init();
	}

	/**
	 * Parse the email templates and replace
	 * $name		UserEntity Name
	 * $email		UserEntity's email
	 * $action_url	${email.api-server.url} + api/changePassword?email=" + user.getEmail() + "&token=" + token;
	 * $hours		${email.password.reset.hours}
	 * 
	 * @param user the user requesting the password reset
	 * @param token	generated token
	 * @param templateName the name of the template found in the resources folder
	 * @return Email Text
	 */
	private String constructResetTokenEmail(final UserDTO user, final String token, final String templateName) {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classloader.getResourceAsStream(templateName);
		
		String email = "";
		String actionUrl = emailApiServerUrl + "api/v2/changePassword?email=" + user.getEmail() + "&token=" + token;

		try {
			byte[] bData = FileCopyUtils.copyToByteArray(inputStream);
		    String template = new String(bData, StandardCharsets.UTF_8);
			VelocityContext context = new VelocityContext();
	        context.put("name", user.getName());
	        context.put("email", user.getEmail());
	        context.put("action_url", actionUrl);
	        context.put("hours", Integer.parseInt(passwordResetHours));
	        
	        StringWriter writer = new StringWriter();
	        Velocity.evaluate(context, writer, "ForgotPassword", template);
			email= writer.toString();
		    
		} catch (IOException e) {
			log.error("Could Not Read Template " + templateName + " : " + e.getMessage());
		}
		
		return email;
	}


	protected String constructContactUsEmail(final String emailFrom, final String message, final String templateName) {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classloader.getResourceAsStream(templateName);
		String email = "";

		try {
			byte[] binaryData = FileCopyUtils.copyToByteArray(inputStream);
		    String template = new String(binaryData, StandardCharsets.UTF_8);
			VelocityContext context = new VelocityContext();
	        context.put("email", emailFrom);
	        context.put("message", message);
	        StringWriter writer = new StringWriter();
	        Velocity.evaluate(context, writer, "ContactUs", template);
	        email = writer.toString();
		} catch (IOException e) {
			log.error("Could Not Read Template " + templateName + "  : " + e.getMessage());
		}
		return email;
	}


	/**
	 * Using AWS SES send out the email using both an HTML and TEXT only format using US_EAST_1
	 *
	 * Email FROM    : ${email.from}
	 * Email SUBJECT : ${email.subject}
	 * EMAIL_ENCODING is 	 : UTF-8
	 * @return SendEmailResult
	 */
	@Override
	public SendEmailResult sendResetEmail(UserDTO user) {

		final PasswordResetTokenDTO token = userService.createPasswordResetTokenForUser(user.getId());

		final String html = constructResetTokenEmail(user, token.getToken(), emailHtmlTemplateReset);
		final String text = constructResetTokenEmail(user, token.getToken(), emailTextTemplateReset);

	      SendEmailRequest request = new SendEmailRequest();
	      request.withDestination(new Destination().withToAddresses(user.getEmail()));
	      request.withMessage(
	    		  new Message().withBody(new Body()
	                      .withHtml(new Content().withCharset(EMAIL_ENCODING).withData(html))
	                      .withText(new Content().withCharset(EMAIL_ENCODING).withData(text)))
	          		      .withSubject(new Content()
	                      .withCharset(EMAIL_ENCODING).withData(emailSubjectReset)))
	              .withSource(emailFromReset);
		return amazonSimpleEmailService.sendEmail(request);
	}

	@Override
	public SendEmailResult sendContactUsEmail(ContactUs contactUsDetails) {

		String html = constructContactUsEmail(contactUsDetails.getEmail(), contactUsDetails.getMessage(), emailHtmlTemplateContact);
		String text = constructContactUsEmail(contactUsDetails.getEmail(), contactUsDetails.getMessage(), emailTextTemplateContact);

		SendEmailRequest request = new SendEmailRequest();
		request.withDestination(new Destination().withToAddresses(emailAdminTo));
		request.withMessage(
				new Message().withBody(new Body()
						.withHtml(new Content().withCharset(EMAIL_ENCODING).withData(html))
						.withText(new Content().withCharset(EMAIL_ENCODING).withData(text)))
						.withSubject(new Content()
						.withCharset(EMAIL_ENCODING).withData(emailSubjectContact)))
						.withSource(emailFromContact);
		return amazonSimpleEmailService.sendEmail(request);

	}
}
