package io.knowledgeassets.myskills.server.notification.command;

import io.knowledgeassets.myskills.server.notification.Notification;
import io.knowledgeassets.myskills.server.notification.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.requireNonNull;

@Service
public class NotificationCommandService {

	private final NotificationRepository notificationRepository;

	public NotificationCommandService(NotificationRepository notificationRepository) {
		this.notificationRepository = requireNonNull(notificationRepository);
	}

	@Transactional
	public Notification save(Notification notification) {
		return notificationRepository.save(notification);
	}

}
