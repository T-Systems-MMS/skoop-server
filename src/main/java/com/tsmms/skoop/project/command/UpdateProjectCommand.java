package com.tsmms.skoop.project.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProjectCommand {

	@NotEmpty
	private String id;
	@NotEmpty
	private String name;
	private String customer;
	private String industrySector;
	private String description;

}
