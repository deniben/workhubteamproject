package com.spring.service.implementations;

import com.spring.dao.UserDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

import javax.transaction.Transactional;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;

import com.spring.component.UserContext;
import com.spring.entity.User;

@Service
public class MailService {

	private final PropertiesService propertiesService;
	private final UserContext userContext;
    private final UserDao userDao;

	@Autowired
	public MailService(PropertiesService propertiesService, UserContext userContext, UserDao userDao) {
		this.userContext = userContext;
		this.propertiesService = propertiesService;
		this.userDao = userDao;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

	private static final String TEXT_MARKER = "TEXT";
	private static final String NICKNAME_MARKER = "NICKNAME";
	private static final String USERNAME_MARKER = "USERNAME";
	private static final String PASSWORD_MARKER = "PASSWORD";
	private static final String LINK_MARKER = "LINK";
	private static final String IP_ADDRESS_MARKER = "IP_ADDRESS";
	private static final String DATE_MARKER = "DATE";
	private static final String COUNTRY_MARKER = "COUNTRY";
	private static final String BROWSER_MARKER = "BROWSER";

	private void send(String email, String subject, String text) {

		JavaMailSender javaMailSender = javaMailSender();

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {

			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setFrom(propertiesService.getSenderAddress());
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(text, true);

			javaMailSender.send(mimeMessageHelper.getMimeMessage());

			LOGGER.info("Mail to " + email + " was sent");

		} catch(Exception e) {
			LOGGER.error(e.toString());
		}

	}

	@Transactional
	public void sendAcception(Long userId) {
		LOGGER.debug("in sendAcception(userId: {})", userId);
		String email = userDao.findById(userId).get().getUsername();
		send(email, "Congratulations!! You have been successfully added to company ", "");

	}

	public void sendPasswordToCreatedUser(String email, String password) {
		String messageText = propertiesService.getMailMessages().get(PropertiesService.USER_CREATED_BY_ADMIN_MAIL);
		messageText = messageText.replace(PASSWORD_MARKER, password).replace(USERNAME_MARKER, email);
		send(email, "Account was created", messageText);
	}


	public void sendActivationMail(String email, Long userId, String token) {
		String messageText = propertiesService.getMailMessages().get(PropertiesService.ACTIVATION_MESSAGE);
		String url = propertiesService.getClientUrl() + "/activate?id=" + userId + "&token=" + token;
		send(email, "Activate your account", messageText.replace(TEXT_MARKER, url));
	}

	public void sendPasswordChangeConfirmationMail(String email, String nickname, String token) {
		String messageText = propertiesService.getMailMessages().get(PropertiesService.CONFIRM_PASSWORD_CHANGE_MESSAGE);
		String url = propertiesService.getClientUrl() + "/change-password?token=" + token;
		messageText = replaceMarkers(messageText, new String[][] {
				{ NICKNAME_MARKER, nickname },
				{ LINK_MARKER, url }
		});
		send(email, "Confirm password change", messageText);
	}

	public void sendPasswordChangedMail(String ipAddress, String country, String browser) {

		String messageText = propertiesService.getMailMessages().get(PropertiesService.PASSWORD_CHANGED_MESSAGE);
		User user = userContext.getCurrentUser();

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd' 'hh' 'a");

		if(browser == null) {
			browser = "Unknown";
		}

		messageText = replaceMarkers(messageText, new String[][] {
				{ NICKNAME_MARKER, user.getProfile().getNickname() },
				{ IP_ADDRESS_MARKER, ipAddress },
				{ DATE_MARKER, LocalDateTime.now().format(dateTimeFormatter) },
				{ COUNTRY_MARKER, country },
				{ BROWSER_MARKER, browser }
		});

		send(user.getUsername(), "Password changed! Attention!", messageText);
	}

	public void sendPasswordRecoveryMail(User user, String token) {

		String messageText = propertiesService.getMailMessages().get(PropertiesService.PASSWORD_RECOVERY_MESSAGE);
		String link = propertiesService.getClientUrl() + "/forgot-password?id=" + user.getId() + "&token=" + token;

		messageText = replaceMarkers(messageText, new String[][] {
				{NICKNAME_MARKER, user.getProfile().getNickname()},
				{LINK_MARKER, link}
		});

		send(user.getUsername(), "Password recovery", messageText);
	}

	private String replaceMarkers(String message, String[]... markerAndValue) {

		if(markerAndValue == null || markerAndValue.length == 0) {
			return message;
		}

		Integer sizeWithoutCurrent = markerAndValue.length - 1;

		String[][] markers = new String[sizeWithoutCurrent][2];

		for(int i = 1; i < markerAndValue.length; i++) {
			markers[i - 1] = new String[] { markerAndValue[i][0], markerAndValue[i][1] };
		}

		return replaceMarkers(message.replace(markerAndValue[0][0], markerAndValue[0][1]), markers);

	}

	@Bean
	public JavaMailSender javaMailSender() {

		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		Map<String, String> mailProperties = propertiesService.getMailProperties();

		javaMailSender.setUsername(mailProperties.get(PropertiesService.MAIL_USERNAME));
		javaMailSender.setPassword(mailProperties.get(PropertiesService.MAIL_PASSWORD));
		javaMailSender.setPort(Integer.parseInt(mailProperties.get(PropertiesService.MAIL_PORT)));
		javaMailSender.setHost(mailProperties.get(PropertiesService.MAIL_HOST));

		Properties properties = javaMailSender.getJavaMailProperties();

		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.debug", "false");

		return javaMailSender;

	}

}
