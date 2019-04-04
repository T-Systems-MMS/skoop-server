package com.tsmms.skoop.community;

import com.tsmms.skoop.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommunityDeletedNotificationConverter implements Converter<CommunityDeletedNotification, AbstractNotificationResponse> {

	@Override
	public CommunityDeletedNotificationResponse convert(CommunityDeletedNotification notification) {
		return CommunityDeletedNotificationResponse.of(notification);
	}
}
