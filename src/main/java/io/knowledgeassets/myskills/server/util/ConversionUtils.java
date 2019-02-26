package io.knowledgeassets.myskills.server.util;

import io.knowledgeassets.myskills.server.community.Link;
import io.knowledgeassets.myskills.server.community.LinkResponse;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillResponse;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserSimpleResponse;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public final class ConversionUtils {

	private ConversionUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static List<LinkResponse> convertLinkListToLinkResponseList(List<Link> links) {
		if (links == null) {
			return Collections.emptyList();
		}
		return links.stream().map(l -> LinkResponse.builder()
				.name(l.getName())
				.href(l.getHref())
				.build()
		).collect(toList());
	}

	public static List<SkillResponse> convertSkillListToSkillResponseList(List<Skill> skills) {
		if (skills == null) {
			return Collections.emptyList();
		}
		return skills.stream().map(SkillResponse::of).collect(toList());
	}

	public static List<UserSimpleResponse> convertUserListToUserSimpleResponseList(List<User> users) {
		if (users == null) {
			return Collections.emptyList();
		}
		return users.stream().map(UserSimpleResponse::of).collect(toList());
	}

}
