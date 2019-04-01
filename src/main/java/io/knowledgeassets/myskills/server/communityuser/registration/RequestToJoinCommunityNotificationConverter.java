package io.knowledgeassets.myskills.server.communityuser.registration;

import io.knowledgeassets.myskills.server.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestToJoinCommunityNotificationConverter implements Converter<RequestToJoinCommunityNotification, AbstractNotificationResponse> {

	@Override
	public RequestToJoinCommunityNotificationResponse convert(RequestToJoinCommunityNotification notification) {
		return RequestToJoinCommunityNotificationResponse.of(notification);
	}
}
