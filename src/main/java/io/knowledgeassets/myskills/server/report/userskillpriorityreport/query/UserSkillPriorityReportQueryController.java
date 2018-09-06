package io.knowledgeassets.myskills.server.report.userskillpriorityreport.query;

import io.knowledgeassets.myskills.server.report.skillpriorityreport.query.SkillPriorityReportQueryService;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReportResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Api(tags = "Priority UserSkillPriorityReport", description = "API allowing queries of reports")
@RestController
public class UserSkillPriorityReportQueryController {
	private UserSkillPriorityReportQueryService userSkillPriorityReportQueryService;
	private SkillPriorityReportQueryService skillPriorityReportQueryService;

	public UserSkillPriorityReportQueryController(UserSkillPriorityReportQueryService userSkillPriorityReportQueryService,
												  SkillPriorityReportQueryService skillPriorityReportQueryService) {
		this.userSkillPriorityReportQueryService = userSkillPriorityReportQueryService;
		this.skillPriorityReportQueryService = skillPriorityReportQueryService;
	}

	@ApiOperation(value = "Get all reports",
			notes = "Get all reports currently stored in the system. The list is unsorted.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/reports", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserSkillPriorityReportResponse> getReports() {
		List<UserSkillPriorityReportResponse> collect = userSkillPriorityReportQueryService.getReports()
				.map(userSkillPriorityReport -> UserSkillPriorityReportResponse.builder()
						.id(userSkillPriorityReport.getId())
						.date(userSkillPriorityReport.getDate())
						.skillCount(skillPriorityReportQueryService.countBySkillReportId(userSkillPriorityReport.getId()))
						.build())
				.collect(toList());
		return collect;
	}

}
