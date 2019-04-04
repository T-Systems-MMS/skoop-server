package com.tsmms.skoop.communityuser.registration;

import com.tsmms.skoop.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestToJoinCommunityNotificationConverter implements Converter<RequestToJoinCommunityNotification, AbstractNotificationResponse> {

	@Override
	public RequestToJoinCommunityNotificationResponse convert(RequestToJoinCommunityNotification notification) {
		return RequestToJoinCommunityNotificationResponse.of(notification);
	}
}
