package com.tsmms.skoop.membership;

import com.tsmms.skoop.skill.SkillResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

import static com.tsmms.skoop.skill.SkillResponse.convertSkillListToSkillResponseList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
		value = "MembershipResponse",
		description = "This holds membership data. It is used to transfer membership data to a client."
)
public class MembershipResponse {

	@ApiModelProperty("Identifier of a membership.")
	private String id;
	@ApiModelProperty("Name of an organisation / web-site / blog the membership is associated with.")
	@NotEmpty
	private String name;
	@ApiModelProperty("Additional information.")
	private String description;
	@ApiModelProperty("Link to organisation / web-site / blog.")
	private String link;
	@ApiModelProperty("The datetime when membership was created.")
	private LocalDateTime creationDatetime;
	@ApiModelProperty("The datetime when membership was last edited.")
	private LocalDateTime lastModifiedDatetime;
	@ApiModelProperty("The skills linked to the membership.")
	private List<SkillResponse> skills;

	public static MembershipResponse of(Membership membership) {
		return MembershipResponse.builder()
				.id(membership.getId())
				.name(membership.getName())
				.description(membership.getDescription())
				.link(membership.getLink())
				.creationDatetime(membership.getCreationDatetime())
				.lastModifiedDatetime(membership.getLastModifiedDatetime())
				.skills(convertSkillListToSkillResponseList(membership.getSkills()))
				.build();
	}

}
