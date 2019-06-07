package com.tsmms.skoop.user;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GlobalUserPermissionConverter implements Converter<GlobalUserPermission, PermissionResponse> {

	@Override
	public GlobalUserPermissionResponse convert(GlobalUserPermission globalUserPermission) {
		return GlobalUserPermissionResponse.of(globalUserPermission);
	}
}
