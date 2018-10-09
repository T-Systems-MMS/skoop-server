package io.knowledgeassets.myskills.server.report.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(
		value = "UserReportSimpleResponse",
		description = "Simple view of user data within a report."
)
public class UserReportSimpleResponse {
	@ApiModelProperty("Unique user name")
	private String userName;

	@ApiModelProperty("First name")
	private String firstName;

	@ApiModelProperty("Last name")
	private String lastName;
}
