package io.knowledgeassets.myskills.server.community;

import io.knowledgeassets.myskills.server.skill.SkillResponse;
import io.knowledgeassets.myskills.server.user.UserSimpleResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static io.knowledgeassets.myskills.server.util.ConversionUtils.convertUserListToUserSimpleResponseList;
import static io.knowledgeassets.myskills.server.util.ConversionUtils.convertSkillListToSkillResponseList;
import static io.knowledgeassets.myskills.server.util.ConversionUtils.convertLinkListToLinkResponseList;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedCommunityResponse {

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

	@ApiModelProperty("Flag indicating if a community is recommended.")
	private boolean recommended;

	public static RecommendedCommunityResponse of(RecommendedCommunity recommendedCommunity) {
		return RecommendedCommunityResponse.builder()
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
		return RecommendedCommunityResponse.builder()
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
