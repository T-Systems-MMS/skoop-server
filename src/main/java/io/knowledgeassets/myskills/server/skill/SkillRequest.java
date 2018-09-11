package io.knowledgeassets.myskills.server.skill;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SkillRequest {

	@NotBlank
	private String name;
	private String description;
}
