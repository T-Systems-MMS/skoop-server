package com.tsmms.skoop.search.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@ApiModel(value = "User skill current levels with an anonymous user reference id.",
	description = "User skill current levels with an anonymous user reference id. It is not possible to determine which user the skills belong to.")
public class AnonymousUserSkillResponse {

	@ApiModelProperty("User reference ID")
	@NotEmpty
	private String userReferenceId;

	@ApiModelProperty("List of up-to-date user skill levels.")
	private List<CurrentSkillLevelResponse> skills;
}
