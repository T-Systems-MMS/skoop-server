package com.tsmms.skoop.userproject.command;

import com.tsmms.skoop.exception.DuplicateResourceException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.project.Project;
import com.tsmms.skoop.project.command.ProjectCommandService;
import com.tsmms.skoop.project.query.ProjectQueryService;
import com.tsmms.skoop.skill.command.SkillCommandService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.userproject.UserProject;
import com.tsmms.skoop.userproject.UserProjectRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;

import static com.tsmms.skoop.exception.enums.Model.USER;
import static com.tsmms.skoop.exception.enums.Model.USER_PROJECT;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Service
public class UserProjectCommandService {

	private final UserProjectRepository userProjectRepository;
	private final ProjectQueryService projectQueryService;
	private final ProjectCommandService projectCommandService;
	private final UserQueryService userQueryService;
	private final SkillCommandService skillCommandService;

	public UserProjectCommandService(UserProjectRepository userProjectRepository, ProjectQueryService projectQueryService, UserQueryService userQueryService,
									 SkillCommandService skillCommandService,
									 ProjectCommandService projectCommandService) {
		this.userProjectRepository = requireNonNull(userProjectRepository);
		this.projectQueryService = requireNonNull(projectQueryService);
		this.userQueryService = requireNonNull(userQueryService);
		this.skillCommandService = requireNonNull(skillCommandService);
		this.projectCommandService = requireNonNull(projectCommandService);
	}

	@Transactional
	@PreAuthorize("isPrincipalUserId(#userId)")
	public UserProject assignProjectToUser(String projectName, String userId, UserProject userProject) {
		final Project project = projectQueryService.getProjectByName(projectName).orElseGet(() -> projectCommandService.create(Project.builder()
				.name(projectName)
				.build()
		));
		final User user = userQueryService.getUserById(userId).orElseThrow(() -> NoSuchResourceException.builder()
				.model(USER)
				.searchParamsMap(new String[]{"id", userId})
				.build());
		userProjectRepository.findByUserIdAndProjectId(userId, project.getId()).ifPresent(up -> {
			throw DuplicateResourceException.builder()
					.message(format("User-project relationship with user id '%s' and project id '%s' already exists", userId, project.getId()))
					.build();
		});
		userProject.setProject(project);
		userProject.setUser(user);
		userProject.setSkills(new HashSet<>(skillCommandService.createNonExistentSkills(userProject.getSkills())));
		return save(userProject);
	}

	@Transactional
	@PreAuthorize("isPrincipalUserId(#userId)")
	public UserProject updateUserProject(String userId, String projectId, UpdateUserProjectCommand command) {
		final UserProject userProject = userProjectRepository.findByUserIdAndProjectId(userId, projectId)
				.orElseThrow(() -> NoSuchResourceException.builder()
						.model(Model.USER_PROJECT)
						.searchParamsMap(new String[]{"userId", userId, "projectId", projectId})
						.build());
		userProject.setRole(command.getRole());
		userProject.setTasks(command.getTasks());
		userProject.setStartDate(command.getStartDate());
		userProject.setEndDate(command.getEndDate());
		userProject.setSkills(new HashSet<>(skillCommandService.createNonExistentSkills(command.getSkills())));
		return save(userProject);
	}

	private UserProject save(UserProject userProject) {
		final LocalDateTime now = LocalDateTime.now();
		userProject.setCreationDate(now);
		userProject.setLastModifiedDate(now);
		return userProjectRepository.save(userProject);
	}

	@Transactional
	@PreAuthorize("isPrincipalUserId(#userId)")
	public void deleteUserProject(String userId, String projectId) {
		final UserProject userProject = userProjectRepository.findByUserIdAndProjectId(userId, projectId).orElseThrow(() -> NoSuchResourceException.builder()
				.model(USER_PROJECT)
				.searchParamsMap(new String[]{"userId", userId, "projectId", projectId})
				.build());
		userProjectRepository.delete(userProject);
	}

}
