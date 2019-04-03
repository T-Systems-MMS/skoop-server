package com.tsmms.skoop.notification.query;

import com.tsmms.skoop.notification.Notification;
import com.tsmms.skoop.notification.NotificationRepository;
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
