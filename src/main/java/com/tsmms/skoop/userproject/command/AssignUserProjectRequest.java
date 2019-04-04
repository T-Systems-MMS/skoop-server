package com.tsmms.skoop.userproject.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@ApiModel(value = "AssignUserProjectRequest"
		, description = "Request object to assign new project to a user.")
@NoArgsConstructor
@AllArgsConstructor
public class AssignUserProjectRequest {

	@NotEmpty
	@ApiModelProperty("An identifier of a project to assign to the user")
	private String projectId;
	@ApiModelProperty("Role the user has/had working on the project")
	private String role;
	@ApiModelProperty("Tasks the user works/worked on")
	private String tasks;
	@NotNull
	@ApiModelProperty("The date when user started working on the project")
	private LocalDate startDate;
	@ApiModelProperty("The date when user finished working on the project")
	private LocalDate endDate;

}
