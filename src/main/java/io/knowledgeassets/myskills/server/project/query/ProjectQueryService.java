package io.knowledgeassets.myskills.server.project.query;

import io.knowledgeassets.myskills.server.project.Project;
import io.knowledgeassets.myskills.server.project.ProjectRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class ProjectQueryService {

	private final ProjectRepository projectRepository;

	public ProjectQueryService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	@Transactional(readOnly = true)
	@PreAuthorize("isAuthenticated()")
	public Stream<Project> getProjects() {
		return StreamSupport.stream(projectRepository.findAll().spliterator(), false);
	}

	@Transactional(readOnly = true)
	@PreAuthorize("isAuthenticated()")
	public Optional<Project> getProjectById(String projectId) {
		return projectRepository.findById(projectId);
	}

}
