package com.tsmms.skoop.user.notification;

import com.tsmms.skoop.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserWelcomeNotificationConverter implements Converter<UserWelcomeNotification, AbstractNotificationResponse> {

	@Override
	public AbstractNotificationResponse convert(UserWelcomeNotification notification) {
		return UserWelcomeNotificationResponse.of(notification);
	}
}
