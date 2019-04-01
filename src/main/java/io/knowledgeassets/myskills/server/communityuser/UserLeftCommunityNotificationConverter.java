package io.knowledgeassets.myskills.server.communityuser;

import io.knowledgeassets.myskills.server.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserLeftCommunityNotificationConverter implements Converter<UserLeftCommunityNotification, AbstractNotificationResponse> {

	@Override
	public UserLeftCommunityNotificationResponse convert(UserLeftCommunityNotification notification) {
		return UserLeftCommunityNotificationResponse.of(notification);
	}
}
