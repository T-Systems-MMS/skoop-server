package com.tsmms.skoop.community;

import com.tsmms.skoop.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommunityChangedNotificationConverter implements Converter<CommunityChangedNotification, AbstractNotificationResponse> {

	@Override
	public CommunityChangedNotificationResponse convert(CommunityChangedNotification notification) {
		return CommunityChangedNotificationResponse.of(notification);
	}
}
