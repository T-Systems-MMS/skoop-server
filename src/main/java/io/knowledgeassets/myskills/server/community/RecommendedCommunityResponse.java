package io.knowledgeassets.myskills.server.community;

import io.knowledgeassets.myskills.server.skill.SkillResponse;
import io.knowledgeassets.myskills.server.user.UserSimpleResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import static io.knowledgeassets.myskills.server.user.UserSimpleResponse.convertUserListToUserSimpleResponseList;
import static io.knowledgeassets.myskills.server.skill.SkillResponse.convertSkillListToSkillResponseList;
import static io.knowledgeassets.myskills.server.community.LinkResponse.convertLinkListToLinkResponseList;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class RecommendedCommunityResponse extends CommunityResponse {

	@ApiModelProperty("Flag indicating if a community is recommended.")
	private final boolean recommended;

	@Builder(builderMethodName = "recommendedCommunityResponseBuilder")
	public RecommendedCommunityResponse(String id,
										String title,
										CommunityType type,
										String description,
										List<LinkResponse> links,
										List<UserSimpleResponse> managers,
										List<UserSimpleResponse> members,
										List<SkillResponse> skills,
										boolean recommended) {
		super(id, title, type, description, links, managers, members, skills);
		this.recommended = recommended;
	}

	public static RecommendedCommunityResponse of(RecommendedCommunity recommendedCommunity) {
		return RecommendedCommunityResponse.recommendedCommunityResponseBuilder()
				.id(recommendedCommunity.getCommunity().getId())
				.title(recommendedCommunity.getCommunity().getTitle())
				.type(recommendedCommunity.getCommunity().getType())
				.description(recommendedCommunity.getCommunity().getDescription())
				.links(convertLinkListToLinkResponseList(recommendedCommunity.getCommunity().getLinks()))
				.managers(convertUserListToUserSimpleResponseList(recommendedCommunity.getCommunity().getManagers()))
				.members(convertUserListToUserSimpleResponseList(recommendedCommunity.getCommunity().getMembers()))
				.skills(convertSkillListToSkillResponseList(recommendedCommunity.getCommunity().getSkills()))
				.recommended(recommendedCommunity.isRecommended())
				.build();
	}

	public static RecommendedCommunityResponse simple(RecommendedCommunity recommendedCommunity) {
		return RecommendedCommunityResponse.recommendedCommunityResponseBuilder()
				.id(recommendedCommunity.getCommunity().getId())
				.title(recommendedCommunity.getCommunity().getTitle())
				.type(recommendedCommunity.getCommunity().getType())
				.description(recommendedCommunity.getCommunity().getDescription())
				.links(convertLinkListToLinkResponseList(recommendedCommunity.getCommunity().getLinks()))
				.managers(convertUserListToUserSimpleResponseList(recommendedCommunity.getCommunity().getManagers()))
				.skills(convertSkillListToSkillResponseList(recommendedCommunity.getCommunity().getSkills()))
				.recommended(recommendedCommunity.isRecommended())
				.build();
	}

}
