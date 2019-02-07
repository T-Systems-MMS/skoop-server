package io.knowledgeassets.myskills.server.project.command;

import io.knowledgeassets.myskills.server.exception.DuplicateResourceException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.project.Project;
import io.knowledgeassets.myskills.server.project.ProjectRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.String.format;

@Service
public class ProjectCommandService {

	private final ProjectRepository projectRepository;

	public ProjectCommandService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	@Transactional
	@PreAuthorize("isAuthenticated()")
	public Project create(Project project) {
		projectRepository.findByNameIgnoreCase(project.getName()).ifPresent(skill -> {
			throw DuplicateResourceException.builder()
					.message(format("Project with name '%s' already exists", project.getName()))
					.build();
		});
		final LocalDateTime now = LocalDateTime.now();
		project.setCreationDate(now);
		project.setLastModifiedDate(now);
		project.setId(UUID.randomUUID().toString());
		return projectRepository.save(project);
	}

	@Transactional
	@PreAuthorize("isAuthenticated()")
	public Project update(Project project) {
		final Project p = projectRepository.findById(project.getId()).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", project.getId()};
			return NoSuchResourceException.builder()
					.model(Model.PROJECT)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		project.setLastModifiedDate(LocalDateTime.now());
		project.setCreationDate(p.getCreationDate());
		return projectRepository.save(project);
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public void delete(String id) {
		final Project project = projectRepository.findById(id).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", id};
			return NoSuchResourceException.builder()
					.model(Model.PROJECT)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		projectRepository.delete(project);
	}

}
