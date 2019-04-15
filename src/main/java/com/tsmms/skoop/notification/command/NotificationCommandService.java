package com.tsmms.skoop.notification.command;

import com.tsmms.skoop.communityuser.registration.AcceptanceToCommunityNotification;
import com.tsmms.skoop.communityuser.registration.CommunityUserRegistrationRepository;
import com.tsmms.skoop.notification.Notification;
import com.tsmms.skoop.notification.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static java.util.Objects.requireNonNull;

@Service
public class NotificationCommandService {

	private final NotificationRepository notificationRepository;
	// TODO Remove dependency on other domain repository
	private final CommunityUserRegistrationRepository communityUserRegistrationRepository;

	public NotificationCommandService(NotificationRepository notificationRepository,
									  CommunityUserRegistrationRepository communityUserRegistrationRepository) {
		this.notificationRepository = requireNonNull(notificationRepository);
		this.communityUserRegistrationRepository = requireNonNull(communityUserRegistrationRepository);
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
		if (notification instanceof AcceptanceToCommunityNotification) {
			communityUserRegistrationRepository.delete(((AcceptanceToCommunityNotification) notification).getRegistration());
		}
		notificationRepository.delete(notification);
	}

	@Transactional
	public void deleteAll(Collection<Notification> notifications) {
		notificationRepository.deleteAll(notifications);
	}

}
