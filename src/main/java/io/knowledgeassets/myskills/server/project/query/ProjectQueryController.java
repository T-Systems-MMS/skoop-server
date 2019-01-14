package io.knowledgeassets.myskills.server.project.query;

import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.project.ProjectResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Api(tags = "Projects", description = "API allowing queries of projects")
@RestController
public class ProjectQueryController {

	private final ProjectQueryService projectQueryService;

	public ProjectQueryController(ProjectQueryService projectQueryService) {
		this.projectQueryService = projectQueryService;
	}

	@ApiOperation(value = "Get all projects",
			notes = "Get all projects currently stored in the system. The list is unsorted.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProjectResponse> getProjects() {
		return projectQueryService.getProjects()
				.map(ProjectResponse::of)
				.collect(toList());
	}

	@ApiOperation(value = "Get a specific project",
			notes = "Get a specific project currently stored in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/projects/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ProjectResponse getProject(@PathVariable("projectId") String projectId) {
		return projectQueryService.getProjectById(projectId)
				.map(ProjectResponse::of)
				.orElseThrow(() -> {
					String[] searchParamsMap = {"id", projectId};
					return NoSuchResourceException.builder()
							.model(Model.PROJECT)
							.searchParamsMap(searchParamsMap)
							.build();
				});
	}

}
