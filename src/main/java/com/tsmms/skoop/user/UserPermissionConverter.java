package com.tsmms.skoop.user;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionConverter implements Converter<UserPermission, PermissionResponse> {

	@Override
	public UserPermissionResponse convert(UserPermission userPermission) {
		return UserPermissionResponse.of(userPermission);
	}
}
