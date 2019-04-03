package com.tsmms.skoop.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("skoop")
public class SkoopProperties {
	private final Security security = new Security();

	public Security getSecurity() {
		return security;
	}

	public class Security {
		private final List<String> defaultRoles = new ArrayList<>();

		public List<String> getDefaultRoles() {
			return defaultRoles;
		}
	}
}
