package com.mealky.rest.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mealky.rest.model.PasswordResetToken;
import com.mealky.rest.model.UserConfirmToken;

@Service
public class EmailSender {
	
	public EmailSender() {
		super();
	}

	@Autowired
	JavaMailSender javaMailSender;

	public void sendConfirmMail(UserConfirmToken u) throws MessagingException
	{
		MimeMessage msg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg , false, "utf-8");
		msg.setContent(contentOfMail(u.getEmailToken(), "To finish registration process click this link below:","confirm"),"text/html");
		helper.setTo(u.getUser().getEmail());
		helper.setSubject("Do not answer to this email");
		helper.setFrom("mealky.noreply@gmail.com");
		javaMailSender.send(msg);
	}

	public void sendResetPassMail(PasswordResetToken u) throws MessagingException
	{
		MimeMessage msg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg , false, "utf-8");
		msg.setContent(contentOfMail(u.getToken(), "To set new password click button below:","form/setpassword"),"text/html");
		helper.setTo(u.getUser().getEmail());
		helper.setSubject("Reset Password");
		helper.setFrom("mealky.noreply@gmail.com");
		javaMailSender.send(msg);
	}
	private String contentOfMail(String token,String message,String were) {
		String val = "<!DOCTYPE HTML>\r\n" + 
				"<html>\r\n" + 
				"<head>\r\n" + 
				"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\r\n" + 
				"<script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\r\n" + 
				"<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" integrity=\"sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q\" crossorigin=\"anonymous\"></script>\r\n" + 
				"<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" integrity=\"sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl\" crossorigin=\"anonymous\"></script>\r\n" + 
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n" + 
				"<style>\r\n" + 
				".div_center{\r\n" + 
				"margin:auto;background:#87cefa\r\n" + 
				"}\r\n" + 
				"img{\r\n" + 
				"display: block;\r\n" + 
				"    margin-left: auto;\r\n" + 
				"    margin-right: auto;\r\n" + 
				"    }\r\n" + 
				"</style>\r\n" + 
				"</head>\r\n" + 
				"<body class=\"p-3 mb-2\" style=\"background:#0F334D\">\r\n" + 
				"<div class=\"container\">\r\n" + 
				"	<div class=\"div_center\">\r\n" + 
				"		<img src=\"https://res.cloudinary.com/mealky/image/upload/v1548102767/email_assets/logo.png\"/>\r\n" + 
				"		<div class=\"d-flex justify-content-center\" style=\"font-size:25px;\">\r\n" + 
				"		"+message+" \r\n" + 
				"		</div>\r\n" + 
				"		<div class=\"d-flex justify-content-center\">\r\n" + 
				"			<a href='https://mealkyapp.herokuapp.com/"+were+"?token="+token+"'>\r\n" + 
				"    	<img src='https://res.cloudinary.com/mealky/image/upload/v1548108573/email_assets/confirm_button.png'/>" + 
				"		</a>\r\n" + 
				"		</div>\r\n" + 
				"		<hr></hr>\r\n" + 
				"		<div class=\"d-flex justify-content-center\">\r\n" + 
				"		Thank you for choosing our application!\r\n" + 
				"		</div>\r\n" + 
				"	</div>\r\n" + 
				"</div>\r\n" + 
				"</body>\r\n" + 
				"</html>";
		return val;
	}
}
