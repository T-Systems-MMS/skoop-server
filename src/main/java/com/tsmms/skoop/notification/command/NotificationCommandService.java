package com.tsmms.skoop.notification.command;

import com.tsmms.skoop.notification.Notification;
import com.tsmms.skoop.notification.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

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

	@Transactional
	public void delete(Notification notification) {
		if (notification == null) {
			throw new IllegalArgumentException("Notification cannot be null.");
		}
		notificationRepository.delete(notification);
	}

	@Transactional
	public void deleteAll(Collection<Notification> notifications) {
		notificationRepository.deleteAll(notifications);
	}

}
