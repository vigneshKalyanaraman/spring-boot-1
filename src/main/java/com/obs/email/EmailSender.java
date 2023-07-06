package com.obs.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

@Component
public class EmailSender {

	public void sendFromCustomerService(String to, String subject, String body) throws MessagingException{
		send(new EmailConfiguration().getUserServiceEmailAddress(), to, null, subject, body);
	}
	
	public void send(String from, String to, String cc, String subject, String body) throws MessagingException{
		Properties props = new Properties();
		   props.put("mail.smtp.auth", new EmailConfiguration().isAuth());
		   props.put("mail.smtp.starttls.enable", "true");
		   props.put("mail.smtp.host", new EmailConfiguration().smtpHost());
		   props.put("mail.smtp.port", new EmailConfiguration().smtpPort());
		   
		   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			   @Override
		      protected PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication(new EmailConfiguration().smtpUser(), new EmailConfiguration().smtpPassword());
		      }
		   });
		   Message msg = new MimeMessage(session);
		   msg.setFrom(new InternetAddress(from, false));
		   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		   msg.setRecipients(Message.RecipientType.CC, cc != null?InternetAddress.parse(cc):null);
		   msg.setSubject(subject);
		   msg.setContent(body, "text/html");
		   msg.setSentDate(new Date());

		   MimeBodyPart messageBodyPart = new MimeBodyPart();
		   messageBodyPart.setContent(body, "text/html");
		   Transport.send(msg);  
	}
	
}
