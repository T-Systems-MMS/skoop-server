package io.knowledgeassets.myskills.server.communityuser;

import io.knowledgeassets.myskills.server.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommunityUserRoleChangedNotificationConverter implements Converter<CommunityUserRoleChangedNotification, AbstractNotificationResponse> {

	@Override
	public CommunityUserRoleChangedNotificationResponse convert(CommunityUserRoleChangedNotification notification) {
		return CommunityUserRoleChangedNotificationResponse.of(notification);
	}

}
