package io.knowledgeassets.myskills.server.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("myskills")
public class MySkillsProperties {
	private final Security security = new Security();

	public Security getSecurity() {
		return security;
	}

	public class Security {
		private final List<String> users = new ArrayList<>();

		public List<String> getUsers() {
			return users;
		}
	}
}
