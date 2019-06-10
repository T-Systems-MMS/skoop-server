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
import static java.lang.String.format;

@Service
public class ManagerNotificationService {

	private final EmailService emailService;

	private final String subjectTemplate;
	private final String contentTemplate;
	private final String subordinateLinkTemplate;

	public ManagerNotificationService(EmailService emailService,
									  @Value("${skoop.email.manager-notification.subject:" +
															 "User project memberships are pending for your approval.}") String subjectTemplate,
									  @Value("${skoop.email.manager-notification.subordinate-link-template:#{null}}") String subordinateLinkTemplate) throws IOException {
		this.emailService = requireNonNull(emailService);
		this.subjectTemplate = requireNonNull(subjectTemplate);
		this.subordinateLinkTemplate = requireNonNull(subordinateLinkTemplate);
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
		final String subordinateLinks = userProjects.stream()
				.collect(toMap(up -> up.getUser().getId(), UserProject::getUser, (firstUser, secondUser) -> firstUser))
				.values()
				.stream()
				.map(u -> format("<a href=\"%s\">%s</a>",
						subordinateLinkTemplate.replace("{subordinateId}", u.getId()),
						u.getFirstName() + " " + u.getLastName()))
				.collect(joining(", "));
		final String content = contentTemplate
				.replace("{managerName}", manager.getFirstName())
				.replace("{subordinateLinks}", subordinateLinks);
		emailService.send(subjectTemplate, content, manager.getEmail());
	}

}
