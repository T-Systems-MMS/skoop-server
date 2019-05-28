package com.tsmms.skoop.userproject.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@ApiModel(value = "UpdateUserProjectRequest"
		, description = "Request object to update user project.")
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProjectRequest {

	@ApiModelProperty("Role the user has/had working on the project")
	private String role;
	@ApiModelProperty("Tasks the user works/worked on")
	private String tasks;
	@NotNull
	@ApiModelProperty("The date when user started working on the project")
	private LocalDate startDate;
	@ApiModelProperty("The date when user finished working on the project")
	private LocalDate endDate;
	@ApiModelProperty("Skills user worked with during the project.")
	private Set<String> skills;
	@ApiModelProperty("The flag indicating if the project membership has been approved.")
	private Boolean approved;

}
