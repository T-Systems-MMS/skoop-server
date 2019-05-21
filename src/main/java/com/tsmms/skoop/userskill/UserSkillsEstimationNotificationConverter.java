package com.tsmms.skoop.userskill;

import com.tsmms.skoop.notification.AbstractNotificationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserSkillsEstimationNotificationConverter implements Converter<UserSkillsEstimationNotification, AbstractNotificationResponse> {

	@Override
	public AbstractNotificationResponse convert(UserSkillsEstimationNotification notification) {
		return UserSkillsEstimationNotificationResponse.of(notification);
	}
}
