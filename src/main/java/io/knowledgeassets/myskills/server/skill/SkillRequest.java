package io.knowledgeassets.myskills.server.skill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "SkillRequest"
		, description = "Request object for creating or updating a skill.")
public class SkillRequest {

	@ApiModelProperty("Name of a skill. It cannot be blank.")
	@NotBlank
	@Size(min = 3, max = 64)
	private String name;

	@ApiModelProperty("Description of a skill.")
	private String description;

	@EqualsAndHashCode.Exclude
	private List<String> groups;
}
