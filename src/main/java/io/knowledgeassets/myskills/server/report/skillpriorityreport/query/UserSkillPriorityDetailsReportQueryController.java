package io.knowledgeassets.myskills.server.report.skillpriorityreport.query;

import io.knowledgeassets.myskills.server.report.skillpriorityreport.UserSkillPriorityReportDetailsResponse;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.query.UserSkillPriorityReportQueryService;
import io.knowledgeassets.myskills.server.skill.SkillResponse;
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
import java.util.stream.Collectors;

@Api(tags = "Priority UserSkillPriorityReport", description = "API allowing queries of reports")
@RestController
public class UserSkillPriorityDetailsReportQueryController {
	private UserSkillPriorityReportQueryService userSkillPriorityReportQueryService;
	private SkillPriorityReportQueryService skillPriorityReportQueryService;

	public UserSkillPriorityDetailsReportQueryController(UserSkillPriorityReportQueryService userSkillPriorityReportQueryService,
														 SkillPriorityReportQueryService skillPriorityReportQueryService) {
		this.userSkillPriorityReportQueryService = userSkillPriorityReportQueryService;
		this.skillPriorityReportQueryService = skillPriorityReportQueryService;
	}

	@ApiOperation(value = "Get a specific userskillpriorityreport",
			notes = "Get a specific user currently stored in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/reports/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserSkillPriorityReportDetailsResponse> getReportDetailsByReportIds(@PathVariable("reportId") String reportId) {

		List<UserSkillPriorityReportDetailsResponse> collect = skillPriorityReportQueryService.getUserSkillPriorityReportDetailsByReportId(reportId)
				.map(userSkillPriorityAggregationReport -> UserSkillPriorityReportDetailsResponse.builder()
						.averagePriority(userSkillPriorityAggregationReport.getAveragePriority())
						.maximumPriority(userSkillPriorityAggregationReport.getMaximumPriority())
						.userCount(userSkillPriorityAggregationReport.getUserCount())
						.skill(SkillResponse.builder()
								.id(userSkillPriorityAggregationReport.getSkillReport().getId())
								.name(userSkillPriorityAggregationReport.getSkillReport().getName())
								.description(userSkillPriorityAggregationReport.getSkillReport().getDescription())
								.build()
						)
						.build())
				.collect(Collectors.toList());
		return collect;
	}
}
