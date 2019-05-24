package com.tsmms.skoop.userproject;

import com.tsmms.skoop.project.ProjectResponse;
import com.tsmms.skoop.skill.SkillResponse;
import com.tsmms.skoop.user.UserSimpleResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static com.tsmms.skoop.skill.SkillResponse.convertSkillListToSkillResponseSet;

@Data
@Builder
@ApiModel(
		value = "UserProjectResponse",
		description = "Relationship between user and project. This data structure is used to pass relationship data to client side."
)
public class UserProjectResponse {

	@ApiModelProperty("An identifier of a relationship")
	private String id;

	@ApiModelProperty("Role the user has/had working on the project")
	private String role;

	@ApiModelProperty("Tasks the user works/worked on")
	private String tasks;

	@NotNull
	@ApiModelProperty("The date when user started working on the project")
	private LocalDate startDate;

	@ApiModelProperty("The date when user finished working on the project")
	private LocalDate endDate;

	@NotNull
	@ApiModelProperty("The date-time when a relationship was created")
	private LocalDateTime creationDate;

	@NotNull
	@ApiModelProperty("The date-time when a relationship was last modified")
	private LocalDateTime lastModifiedDate;

	@ApiModelProperty("The use project is assigned to")
	private UserSimpleResponse user;

	@ApiModelProperty("The assigned project")
	private ProjectResponse project;

	@ApiModelProperty("Skills user worked with during the project.")
	private Set<SkillResponse> skills;

	@ApiModelProperty("Flag indicating if project membership was approved.")
	private Boolean approved;

	public static UserProjectResponse of(UserProject userProject) {
		return UserProjectResponse.builder()
				.id(userProject.getId())
				.role(userProject.getRole())
				.tasks(userProject.getTasks())
				.startDate(userProject.getStartDate())
				.endDate(userProject.getEndDate())
				.creationDate(userProject.getCreationDate())
				.lastModifiedDate(userProject.getLastModifiedDate())
				.project(ProjectResponse.of(userProject.getProject()))
				.user(UserSimpleResponse.of(userProject.getUser()))
				.skills(convertSkillListToSkillResponseSet(userProject.getSkills()))
				.approved(userProject.isApproved())
				.build();
	}

}
