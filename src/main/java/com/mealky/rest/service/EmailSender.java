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
		String htmlmsg = "To finish registration process click this link below:</br> <a href='https://mealkyapp.herokuapp.com/confirm?token="+u.getEmailToken()+"'>Click</a>";
		msg.setContent(htmlmsg,"text/html");
		helper.setTo(u.getUser().getEmail());
		helper.setSubject("Do not answer to this email");
		helper.setFrom("mealky.noreply@gmail.com");
		javaMailSender.send(msg);
	}
	//</br> <a href='https://mealkyapp.herokuapp.com/confirm?token="+u.getEmailToken()+"'>Click</a>";
	//<a href='https://mealkyapp.herokuapp.com/form/setpassword?token="+u.getToken()+"'>Click</a>";
	public void sendResetPassMail(PasswordResetToken u) throws MessagingException
	{
		MimeMessage msg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg , false, "utf-8");
		String htmlmsg = "To generate new password click this link: <a href='https://mealkyapp.herokuapp.com/form/setpassword?token="+u.getToken()+">Click</a>";
		msg.setContent(htmlmsg,"text/html");
		helper.setTo(u.getUser().getEmail());
		helper.setSubject("Reset Password");
		helper.setFrom("mealky.noreply@gmail.com");
		javaMailSender.send(msg);
	}
}
