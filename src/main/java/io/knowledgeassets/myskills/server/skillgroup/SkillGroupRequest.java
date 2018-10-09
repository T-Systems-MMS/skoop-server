package io.knowledgeassets.myskills.server.skillgroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "GroupRequest"
		, description = "Request object for creating or updating a skill group.")
public class SkillGroupRequest {

	@ApiModelProperty("Name of a skill group. It cannot be blank.")
	@NotBlank
	@Size(min = 3, max = 64)
	private String name;

	@ApiModelProperty("Description of a skill group.")
	private String description;
}
