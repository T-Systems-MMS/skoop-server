package io.knowledgeassets.myskills.server.community;

import io.knowledgeassets.myskills.server.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommunityDeletedNotificationConverter implements Converter<CommunityDeletedNotification, AbstractNotificationResponse> {

	@Override
	public CommunityDeletedNotificationResponse convert(CommunityDeletedNotification notification) {
		return CommunityDeletedNotificationResponse.of(notification);
	}
}
