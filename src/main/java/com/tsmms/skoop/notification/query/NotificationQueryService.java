package com.tsmms.skoop.notification.query;

import com.tsmms.skoop.notification.Notification;
import com.tsmms.skoop.notification.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@Service
public class NotificationQueryService {

	private final NotificationRepository notificationRepository;

	public NotificationQueryService(NotificationRepository notificationRepository) {
		this.notificationRepository = requireNonNull(notificationRepository);
	}

	@Transactional(readOnly = true)
	public Optional<Notification> getNotification(String notificationId) {
		return notificationRepository.findById(notificationId);
	}

	@Transactional(readOnly = true)
	public Stream<Notification> getUserNotifications(String userId) {
		return notificationRepository.getUserNotifications(userId);
	}

	@Transactional(readOnly = true)
	public int getUserNotificationCounter(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("User ID cannot be null.");
		}
		return notificationRepository.getUserNotificationCounter(userId);
	}

	@Transactional(readOnly = true)
	public Stream<Notification> getNotificationsByCommunityUserRegistrationId(String registrationId) {
		return notificationRepository.findByRegistrationId(registrationId);
	}

}
