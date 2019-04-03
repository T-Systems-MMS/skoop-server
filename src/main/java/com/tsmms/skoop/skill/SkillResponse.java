package com.tsmms.skoop.skill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
		value = "SkillResponse",
		description = "This holds information of a skill. It will be used for sending skill information to client."
)

public class SkillResponse {
	@ApiModelProperty("Skill id")
	private String id;

	@ApiModelProperty("Name of a skill. It cannot be blank.")
	private String name;

	@ApiModelProperty("Description of a skill.")
	private String description;

	@EqualsAndHashCode.Exclude
	private List<String> skillGroups;

	public static SkillResponse of(Skill skill) {
		return SkillResponse.builder()
				.id(skill.getId())
				.name(skill.getName())
				.description(skill.getDescription())
				.build();
	}

	public static List<SkillResponse> convertSkillListToSkillResponseList(List<Skill> skills) {
		if (skills == null) {
			return Collections.emptyList();
		}
		return skills.stream().map(SkillResponse::of).collect(toList());
	}

}
