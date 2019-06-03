package com.tsmms.skoop.email;

import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * Sends an e-mail with the specified subject and content to the recipients.
	 * @param subject - subject
	 * @param content - content
	 * @param to - recipients
	 */
	public void send(final String subject, final String content, final String... to) {
		try {
			final MimeMessage mimeMessage = mailSender.createMimeMessage();
			mimeMessage.setContent(content, "text/html");
			final MimeMessageHelper mail = new MimeMessageHelper(mimeMessage);
			mail.setTo(to);
			mail.setSubject(subject);
			mail.setText(content, true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new MailSendException("E-mail sending failed.", e);
		}
	}

}
