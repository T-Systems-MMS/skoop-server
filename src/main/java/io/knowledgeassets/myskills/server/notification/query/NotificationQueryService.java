package io.knowledgeassets.myskills.server.notification.query;

import io.knowledgeassets.myskills.server.notification.Notification;
import io.knowledgeassets.myskills.server.notification.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@Service
public class NotificationQueryService {

	private final NotificationRepository notificationRepository;

	public NotificationQueryService(NotificationRepository notificationRepository) {
		this.notificationRepository = requireNonNull(notificationRepository);
	}

	@Transactional(readOnly = true)
	public Stream<Notification> getUserNotifications(String userId) {
		return notificationRepository.getUserNotifications(userId);
	}

}
