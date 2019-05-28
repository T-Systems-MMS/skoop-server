package com.tsmms.skoop.userproject;

import com.tsmms.skoop.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserProjectNeedsApprovalNotificationConverter implements Converter<UserProjectNeedsApprovalNotification, AbstractNotificationResponse> {

	@Override
	public AbstractNotificationResponse convert(UserProjectNeedsApprovalNotification notification) {
		return UserProjectNeedsApprovalNotificationResponse.of(notification);
	}
}
