package io.knowledgeassets.myskills.server.communityuser.notification.kickout;

import io.knowledgeassets.myskills.server.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserKickedOutFromCommunityNotificationConverter implements Converter<UserKickedOutFromCommunityNotification, AbstractNotificationResponse> {

	@Override
	public UserKickedOutFromCommunityNotificationResponse convert(UserKickedOutFromCommunityNotification notification) {
		return UserKickedOutFromCommunityNotificationResponse.of(notification);
	}
}
