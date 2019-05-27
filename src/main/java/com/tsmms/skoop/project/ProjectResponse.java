package com.tsmms.skoop.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
		value = "ProjectResponse",
		description = "This holds project data. It is used to transfer project data to a client."
)
public class ProjectResponse {

	@ApiModelProperty("Identifier of a project.")
	private String id;
	@ApiModelProperty("Name of a project. It cannot be blank.")
	private String name;
	@ApiModelProperty("The customer the project is done for.")
	private String customer;
	@ApiModelProperty("The industry sector the project is done for.")
	private String industrySector;
	@ApiModelProperty("Description of a project.")
	private String description;
	@ApiModelProperty("Creation date (read only).")
	private LocalDateTime creationDate;
	@ApiModelProperty("Last modified date (read only).")
	private LocalDateTime lastModifiedDate;

	public static ProjectResponse of(Project project) {
		if (project == null) {
			return null;
		}
		return ProjectResponse.builder()
				.id(project.getId())
				.name(project.getName())
				.customer(project.getCustomer())
				.industrySector(project.getIndustrySector())
				.description(project.getDescription())
				.lastModifiedDate(project.getLastModifiedDate())
				.creationDate(project.getCreationDate())
				.build();
	}

}
