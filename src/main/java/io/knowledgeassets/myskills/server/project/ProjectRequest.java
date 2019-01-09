package io.knowledgeassets.myskills.server.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(
		value = "ProjectRequest",
		description = "Request object to create or update project."
)
public class ProjectRequest {

	@ApiModelProperty("Name of a project. It cannot be blank.")
	@NotEmpty
	private String name;
	@ApiModelProperty("The customer the project is done for.")
	private String customer;
	@ApiModelProperty("The industry sector the project is done for.")
	private String industrySector;
	@ApiModelProperty("Description of a project.")
	private String description;

}
