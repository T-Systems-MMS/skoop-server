package io.knowledgeassets.myskills.server.skill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

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

}
