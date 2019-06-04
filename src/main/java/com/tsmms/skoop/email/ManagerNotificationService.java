package com.tsmms.skoop.email;

import com.tsmms.skoop.user.User;
import com.tsmms.skoop.userproject.UserProject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

import static java.util.stream.Collectors.joining;
import static java.util.Objects.requireNonNull;
import static java.nio.charset.StandardCharsets.UTF_8;

import static java.util.stream.Collectors.toMap;
import static org.springframework.util.StreamUtils.copyToString;

@Service
public class ManagerNotificationService {

	private final EmailService emailService;

	private final String subjectTemplate;
	private final String contentTemplate;
	private final String applicationLink;

	public ManagerNotificationService(EmailService emailService,
									  @Value("${skoop.email.manager-notification.subject:" +
															 "User project memberships are pending for your approval.}") String subjectTemplate,
									  @Value("${skoop.email.manager-notification.app-link:#{null}}") String applicationLink) throws IOException {
		this.emailService = requireNonNull(emailService);
		this.subjectTemplate = requireNonNull(subjectTemplate);
		this.applicationLink = requireNonNull(applicationLink);
		final ClassPathResource contentTemplateFile = new ClassPathResource("templates/manager-notification-template.html");
		this.contentTemplate = copyToString(contentTemplateFile.getInputStream(), UTF_8);
	}

	public void send(User manager, Collection<UserProject> userProjects) {
		if (manager == null) {
			throw new IllegalArgumentException("Manager cannot be null.");
		}
		if (userProjects == null) {
			throw new IllegalArgumentException("User project membership collection cannot be null.");
		}
		if (userProjects.isEmpty()) {
			return;
		}
		final String userNames = userProjects.stream()
				.collect(toMap(up -> up.getUser().getId(), UserProject::getUser, (firstUser, secondUser) -> firstUser))
				.values()
				.stream()
				.map(u -> u.getFirstName() + " " + u.getLastName())
				.collect(joining(", "));
		final String content = contentTemplate
				.replace("{userNames}", userNames)
				.replace("{managerName}", manager.getFirstName())
				.replace("{applicationLink}", applicationLink);
		emailService.send(subjectTemplate, content, manager.getEmail());
	}

}
