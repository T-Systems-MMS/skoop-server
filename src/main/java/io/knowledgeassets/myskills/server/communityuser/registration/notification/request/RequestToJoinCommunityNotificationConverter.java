package io.knowledgeassets.myskills.server.communityuser.registration.notification.request;

import io.knowledgeassets.myskills.server.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestToJoinCommunityNotificationConverter implements Converter<RequestToJoinCommunityNotification, AbstractNotificationResponse> {

	@Override
	public AbstractNotificationResponse convert(RequestToJoinCommunityNotification notification) {
		return RequestToJoinCommunityNotificationResponse.of(notification);
	}
}
