package io.knowledgeassets.myskills.server.skill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "SkillRequest"
		, description = "Request object for creating or updating a skill.")
public class SkillRequest {

	@ApiModelProperty("Name of a skill. It cannot be blank.")
	@NotBlank
	private String name;

	@ApiModelProperty("Description of a skill.")
	private String description;
}
