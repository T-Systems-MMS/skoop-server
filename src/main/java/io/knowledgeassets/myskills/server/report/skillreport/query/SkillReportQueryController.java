package io.knowledgeassets.myskills.server.report.skillreport.query;

import io.knowledgeassets.myskills.server.report.skillreport.SkillReportQueryService;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReportQueryService;
import io.knowledgeassets.myskills.server.skill.SkillResponse;
import io.knowledgeassets.myskills.server.user.UserResponse;
import io.knowledgeassets.myskills.server.userskill.query.SkillUserResponse;
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

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Api(tags = "Priority UserSkillPriorityReport", description = "API allowing queries of reports")
@RestController
public class SkillReportQueryController {
	private SkillReportQueryService skillReportQueryService;

	public SkillReportQueryController(SkillReportQueryService skillReportQueryService) {
		this.skillReportQueryService = skillReportQueryService;
	}

	@ApiOperation(value = "Get a specific skill",
			notes = "Get a specific skill currently stored in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/reports/skill/{skillId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public SkillResponse getSkill(@PathVariable("skillId") String userSkillReportId) {
		return skillReportQueryService.getSkillReportById(userSkillReportId)
				.map(skillReport -> SkillResponse.builder()
						.id(skillReport.getId())
						.name(skillReport.getName())
						.description(skillReport.getDescription())
						.build()
				)
				.orElseThrow(() -> new IllegalArgumentException(format("Skill with ID '%s' not found", userSkillReportId)));
	}
}
