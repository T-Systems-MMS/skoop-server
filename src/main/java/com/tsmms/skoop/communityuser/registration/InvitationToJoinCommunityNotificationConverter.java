package com.tsmms.skoop.communityuser.registration;

import com.tsmms.skoop.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class InvitationToJoinCommunityNotificationConverter implements Converter<InvitationToJoinCommunityNotification, AbstractNotificationResponse> {

	@Override
	public InvitationToJoinCommunityNotificationResponse convert(InvitationToJoinCommunityNotification notification) {
		return InvitationToJoinCommunityNotificationResponse.of(notification);
	}
}
