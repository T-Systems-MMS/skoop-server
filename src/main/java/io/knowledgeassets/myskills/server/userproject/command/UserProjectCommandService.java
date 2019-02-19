package io.knowledgeassets.myskills.server.userproject.command;

import io.knowledgeassets.myskills.server.exception.DuplicateResourceException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.project.Project;
import io.knowledgeassets.myskills.server.project.query.ProjectQueryService;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import io.knowledgeassets.myskills.server.userproject.UserProject;
import io.knowledgeassets.myskills.server.userproject.UserProjectRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static io.knowledgeassets.myskills.server.exception.enums.Model.USER;
import static io.knowledgeassets.myskills.server.exception.enums.Model.USER_PROJECT;
import static java.lang.String.format;

@Service
public class UserProjectCommandService {

	private final UserProjectRepository userProjectRepository;
	private final ProjectQueryService projectQueryService;
	private final UserQueryService userQueryService;

	public UserProjectCommandService(UserProjectRepository userProjectRepository, ProjectQueryService projectQueryService, UserQueryService userQueryService) {
		this.userProjectRepository = userProjectRepository;
		this.projectQueryService = projectQueryService;
		this.userQueryService = userQueryService;
	}

	@Transactional
	@PreAuthorize("isPrincipalUserId(#userId)")
	public UserProject assignProjectToUser(String projectId, String userId, UserProject userProject) {
		final Project project = projectQueryService.getProjectById(projectId).orElseThrow(() -> {
			String[] searchParamsMap = {"id", projectId};
			return NoSuchResourceException.builder()
					.model(Model.PROJECT)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		final User user = userQueryService.getUserById(userId).orElseThrow(() -> NoSuchResourceException.builder()
				.model(USER)
				.searchParamsMap(new String[]{"id", userId})
				.build());
		userProjectRepository.findByUserIdAndProjectId(userId, projectId).ifPresent(up -> {
			throw DuplicateResourceException.builder()
					.message(format("User-project relationship with user id '%s' and project id '%s' already exists", userId, projectId))
					.build();
		});
		userProject.setProject(project);
		userProject.setUser(user);
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
