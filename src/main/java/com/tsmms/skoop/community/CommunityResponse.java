package com.tsmms.skoop.community;

import com.tsmms.skoop.community.link.LinkResponse;
import com.tsmms.skoop.communityuser.CommunityUser;
import com.tsmms.skoop.skill.SkillResponse;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserSimpleResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tsmms.skoop.skill.SkillResponse.convertSkillListToSkillResponseSet;
import static com.tsmms.skoop.user.UserSimpleResponse.convertUserListToUserSimpleResponseList;
import static com.tsmms.skoop.community.link.LinkResponse.convertLinkListToLinkResponseList;

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

	@ApiModelProperty("List of skills associated with a community.")
	private Set<SkillResponse> skills;

	public static CommunityResponse of(Community community) {
		if (community == null) {
			return null;
		}
		return CommunityResponse.builder()
				.id(community.getId())
				.title(community.getTitle())
				.type(community.getType())
				.description(community.getDescription())
				.links(convertLinkListToLinkResponseList(community.getLinks()))
				.managers(convertUserListToUserSimpleResponseList(getManagers(community.getCommunityUsers())))
				.skills(convertSkillListToSkillResponseSet(community.getSkills()))
				.build();
	}

	private static List<User> getManagers(List<CommunityUser> users) {
		if (users == null) {
			return Collections.emptyList();
		} else {
			return users.stream().filter(communityUser -> CommunityRole.MANAGER.equals(communityUser.getRole()))
					.map(CommunityUser::getUser).collect(Collectors.toList());
		}
	}

}
