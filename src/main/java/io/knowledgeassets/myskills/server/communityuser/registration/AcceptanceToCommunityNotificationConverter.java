package io.knowledgeassets.myskills.server.communityuser.registration;

import io.knowledgeassets.myskills.server.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AcceptanceToCommunityNotificationConverter implements Converter<AcceptanceToCommunityNotification, AbstractNotificationResponse> {

	@Override
	public AcceptanceToCommunityNotificationResponse convert(AcceptanceToCommunityNotification notification) {
		return AcceptanceToCommunityNotificationResponse.of(notification);
	}
}
