package io.knowledgeassets.myskills.server.skillgroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "GroupResponse"
		, description = "This holds information of a skill group. It will be used for sending skill group information to client.")
public class SkillGroupResponse {

	@ApiModelProperty("Skill group id")
	private String id;

	@ApiModelProperty("Name of a skill group. It cannot be blank.")
	private String name;

	@ApiModelProperty("Description of a skill group.")
	private String description;
}
