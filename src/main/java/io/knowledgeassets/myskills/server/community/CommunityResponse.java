package io.knowledgeassets.myskills.server.community;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillResponse;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserSimpleResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
		value = "CommunityResponse",
		description = "This holds community data. It is used to transfer community data to a client."
)
public class CommunityResponse {

	@ApiModelProperty("Identifier of a community.")
	private String id;

	@ApiModelProperty("Title of a community.")
	private String title;

	@ApiModelProperty("Type of a community.")
	private CommunityType type;

	@ApiModelProperty("Description of a community.")
	private String description;

	@ApiModelProperty("Links of a community.")
	private List<LinkResponse> links;

	@ApiModelProperty("List of community managers.")
	private List<UserSimpleResponse> managers;

	@ApiModelProperty("List of community members.")
	private List<UserSimpleResponse> members;

	@ApiModelProperty("List of skills associated with a community.")
	private List<SkillResponse> skills;

	public static CommunityResponse of(Community community) {
		return CommunityResponse.builder()
				.id(community.getId())
				.title(community.getTitle())
				.type(community.getType())
				.description(community.getDescription())
				.links(convertLinkListToLinkResponseList(community.getLinks()))
				.managers(convertUserListToUserSimpleResponseList(community.getManagers()))
				.members(convertUserListToUserSimpleResponseList(community.getMembers()))
				.skills(convertSkillListToSkillResponseList(community.getSkills()))
				.build();
	}

	public static CommunityResponse simple(Community community) {
		return CommunityResponse.builder()
				.id(community.getId())
				.title(community.getTitle())
				.type(community.getType())
				.description(community.getDescription())
				.links(convertLinkListToLinkResponseList(community.getLinks()))
				.managers(convertUserListToUserSimpleResponseList(community.getManagers()))
				.skills(convertSkillListToSkillResponseList(community.getSkills()))
				.build();
	}

	private static List<LinkResponse> convertLinkListToLinkResponseList(List<Link> links) {
		if (links == null) {
			return Collections.emptyList();
		}
		return links.stream().map(l -> LinkResponse.builder()
				.name(l.getName())
				.href(l.getHref())
				.build()
		).collect(toList());
	}

	private static List<SkillResponse> convertSkillListToSkillResponseList(List<Skill> skills) {
		if (skills == null) {
			return Collections.emptyList();
		}
		return skills.stream().map(SkillResponse::of).collect(toList());
	}

	private static List<UserSimpleResponse> convertUserListToUserSimpleResponseList(List<User> users) {
		if (users == null) {
			return Collections.emptyList();
		}
		return users.stream().map(UserSimpleResponse::of).collect(toList());
	}

}
