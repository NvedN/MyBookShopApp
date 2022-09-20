package com.example.MyBookShopApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig
{

		@Value("${appEmail.email}")
		private String email;


		@Value("${appEmail.password}")
		private String password;

		@Bean
		public JavaMailSender getJavaMailSender(){
				JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
				mailSender.setHost("smtp.mail.ru");
				mailSender.setPort(465);
				mailSender.setUsername(email);
				mailSender.setPassword(password);
				Properties props = mailSender.getJavaMailProperties();
				props.put("mail.transport.protocol","smtps");
				props.put("mail.smtp.auth","true");
				props.put("mail.smtp.starttls.enable","true");
				props.put("mail.smtp.ssl.enable","true");
				props.put("mail.debug","true");

				return mailSender;
		}

}
