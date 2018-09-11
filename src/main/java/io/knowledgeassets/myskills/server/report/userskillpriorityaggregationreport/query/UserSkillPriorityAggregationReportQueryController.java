package io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.query;

import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.exception.EmptyInputException;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityReportDetailsResponse;
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

import static java.lang.String.format;

@Api(tags = "Priority UserSkillPriorityAggregationReport", description = "API allowing queries details of a specific report")
@RestController
public class UserSkillPriorityAggregationReportQueryController {
	private UserSkillPriorityReportQueryService userSkillPriorityReportQueryService;
	private UserSkillPriorityAggregationReportQueryService userSkillPriorityAggregationReportQueryService;

	public UserSkillPriorityAggregationReportQueryController(UserSkillPriorityReportQueryService userSkillPriorityReportQueryService,
															 UserSkillPriorityAggregationReportQueryService userSkillPriorityAggregationReportQueryService) {
		this.userSkillPriorityReportQueryService = userSkillPriorityReportQueryService;
		this.userSkillPriorityAggregationReportQueryService = userSkillPriorityAggregationReportQueryService;
	}

	@ApiOperation(value = "Get details of a a specific report",
			notes = "Get details of a a specific report, currently stored in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/reports/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserSkillPriorityReportDetailsResponse> getReportDetailsByReportId(@PathVariable("reportId") String reportId) throws BusinessException {
		return userSkillPriorityAggregationReportQueryService.getUserSkillPriorityAggregationReportsByReportId(reportId)
				.map(userSkillPriorityAggregationReport -> UserSkillPriorityReportDetailsResponse.builder()
						.id(userSkillPriorityAggregationReport.getId())
						.averagePriority(userSkillPriorityAggregationReport.getAveragePriority())
						.maximumPriority(userSkillPriorityAggregationReport.getMaximumPriority())
						.userCount(userSkillPriorityAggregationReport.getUserCount())
						.skill(SkillResponse.builder()
								.name(userSkillPriorityAggregationReport.getSkillName())
								.description(userSkillPriorityAggregationReport.getSkillDescription())
								.build()
						)
						.build())
				.collect(Collectors.toList());
	}
}
