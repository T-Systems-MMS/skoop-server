package com.tsmms.skoop.communityuser;

import com.tsmms.skoop.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommunityUserRoleChangedNotificationConverter implements Converter<CommunityUserRoleChangedNotification, AbstractNotificationResponse> {

	@Override
	public CommunityUserRoleChangedNotificationResponse convert(CommunityUserRoleChangedNotification notification) {
		return CommunityUserRoleChangedNotificationResponse.of(notification);
	}

}
