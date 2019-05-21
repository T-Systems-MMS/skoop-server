package com.tsmms.skoop.userproject.command;

import com.tsmms.skoop.exception.DuplicateResourceException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.notification.command.NotificationCommandService;
import com.tsmms.skoop.project.Project;
import com.tsmms.skoop.project.command.ProjectCommandService;
import com.tsmms.skoop.project.query.ProjectQueryService;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.command.SkillCommandService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.userproject.UserProject;
import com.tsmms.skoop.userproject.UserProjectRepository;
import com.tsmms.skoop.userskill.UserSkillsEstimationNotification;
import com.tsmms.skoop.userskill.command.UserSkillCommandService;
import com.tsmms.skoop.userskill.query.UserSkillQueryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
	private final UserSkillCommandService userSkillCommandService;
	private final UserSkillQueryService userSkillQueryService;
	private final NotificationCommandService notificationCommandService;

	public UserProjectCommandService(UserProjectRepository userProjectRepository, ProjectQueryService projectQueryService, UserQueryService userQueryService,
									 SkillCommandService skillCommandService,
									 ProjectCommandService projectCommandService,
									 UserSkillCommandService userSkillCommandService,
									 UserSkillQueryService userSkillQueryService,
									 NotificationCommandService notificationCommandService) {
		this.userProjectRepository = requireNonNull(userProjectRepository);
		this.projectQueryService = requireNonNull(projectQueryService);
		this.userQueryService = requireNonNull(userQueryService);
		this.skillCommandService = requireNonNull(skillCommandService);
		this.projectCommandService = requireNonNull(projectCommandService);
		this.userSkillCommandService = requireNonNull(userSkillCommandService);
		this.userSkillQueryService = requireNonNull(userSkillQueryService);
		this.notificationCommandService = requireNonNull(notificationCommandService);
	}

	private Set<Skill> createNewUserSkills(String userId, Set<Skill> skills) {
		final Set<Skill> newSkills = new HashSet<>();
		skills.forEach(skill -> {
			if (userSkillQueryService.getUserSkillByUserIdAndSkillId(userId, skill.getId()).isEmpty()) {
				userSkillCommandService.createUserSkillBySkillId(userId, skill.getId(), 0, 0, 0);
				newSkills.add(skill);
			}
		});
		return newSkills;
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
		userProject.setSkills(skillCommandService.createNonExistentSkills(userProject.getSkills()));
		final Set<Skill> newSkills = createNewUserSkills(user.getId(), userProject.getSkills());
		if (!newSkills.isEmpty()) {
			notificationCommandService.save(UserSkillsEstimationNotification.builder()
					.id(UUID.randomUUID().toString())
					.creationDatetime(LocalDateTime.now())
					.user(user)
					.skills(newSkills)
					.build()
			);
		}
		final LocalDateTime now = LocalDateTime.now();
		userProject.setId(UUID.randomUUID().toString());
		userProject.setCreationDate(now);
		userProject.setLastModifiedDate(now);
		return userProjectRepository.save(userProject);
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
		final User user = userProject.getUser();
		userProject.setSkills(skillCommandService.createNonExistentSkills(command.getSkills()));
		final Set<Skill> newSkills = createNewUserSkills(userId, userProject.getSkills());
		if (!newSkills.isEmpty()) {
			notificationCommandService.save(UserSkillsEstimationNotification.builder()
					.id(UUID.randomUUID().toString())
					.creationDatetime(LocalDateTime.now())
					.user(user)
					.skills(newSkills)
					.build()
			);
		}
		final LocalDateTime now = LocalDateTime.now();
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
