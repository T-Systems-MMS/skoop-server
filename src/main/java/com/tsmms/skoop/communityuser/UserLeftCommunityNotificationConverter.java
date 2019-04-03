package com.tsmms.skoop.communityuser;

import com.tsmms.skoop.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserLeftCommunityNotificationConverter implements Converter<UserLeftCommunityNotification, AbstractNotificationResponse> {

	@Override
	public UserLeftCommunityNotificationResponse convert(UserLeftCommunityNotification notification) {
		return UserLeftCommunityNotificationResponse.of(notification);
	}
}
