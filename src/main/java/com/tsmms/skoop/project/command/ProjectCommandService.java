package com.tsmms.skoop.project.command;

import com.tsmms.skoop.exception.DuplicateResourceException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.project.Project;
import com.tsmms.skoop.project.ProjectRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Service
public class ProjectCommandService {

	private final ProjectRepository projectRepository;

	public ProjectCommandService(ProjectRepository projectRepository) {
		this.projectRepository = requireNonNull(projectRepository);
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
	public Project update(UpdateProjectCommand updateProjectCommand) {
		final Project p = projectRepository.findById(updateProjectCommand.getId()).orElseThrow(() -> {
			final String[] searchParamsMap = {"id", updateProjectCommand.getId()};
			return NoSuchResourceException.builder()
					.model(Model.PROJECT)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		p.setCustomer(updateProjectCommand.getCustomer());
		p.setDescription(updateProjectCommand.getDescription());
		p.setName(updateProjectCommand.getName());
		p.setIndustrySector(updateProjectCommand.getIndustrySector());
		p.setLastModifiedDate(LocalDateTime.now());
		if (p.getUserProjects() != null) {
			p.getUserProjects().forEach(up -> up.setApproved(false));
		}
		return projectRepository.save(p);
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
