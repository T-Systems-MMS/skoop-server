package com.tsmms.skoop.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class EmailServiceTests {

	@Mock
	private JavaMailSender javaMailSender;

	private EmailService emailService;

	@BeforeEach
	void setUp() {
		this.emailService = new EmailService(javaMailSender);
	}

	@DisplayName("Sends an e-mail.")
	@Test
	void sendEmail() {
		given(javaMailSender.createMimeMessage()).willReturn(mock(MimeMessage.class));
		assertDoesNotThrow(() -> emailService.send("Subject", "Content", "user@skoop.io"));
	}

}
