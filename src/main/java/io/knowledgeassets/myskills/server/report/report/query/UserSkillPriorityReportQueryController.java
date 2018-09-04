package io.knowledgeassets.myskills.server.report.report.query;

import io.knowledgeassets.myskills.server.report.priorityreportdetails.UserSkillPriorityReportDetailsResponse;
import io.knowledgeassets.myskills.server.report.report.UserSkillPriorityReportResponse;
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
public class UserSkillPriorityReportQueryController {
	private UserSkillPriorityReportQueryService userSkillPriorityReportQueryService;

	public UserSkillPriorityReportQueryController(UserSkillPriorityReportQueryService userSkillPriorityReportQueryService) {
		this.userSkillPriorityReportQueryService = userSkillPriorityReportQueryService;
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
						.skillCount(userSkillPriorityReport.getUserSkillPriorityReportDetails().size()) // TODO: 9/4/2018 getSkillCount
						.build())
				.collect(toList());
		return collect;
	}

	@ApiOperation(value = "Get a specific report",
			notes = "Get a specific user currently stored in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/reports/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserSkillPriorityReportDetailsResponse getUserById(@PathVariable("reportId") String reportId) {
		return userSkillPriorityReportQueryService.getUserSkillPriorityReportDetailsByReportId(reportId)
				.map(userSkillPriorityReport -> UserSkillPriorityReportDetailsResponse.builder()
						.id(userSkillPriorityReport.getId())
						.averagePriority(userSkillPriorityReport.getAveragePriority())
						.maximumPriority(userSkillPriorityReport.getMaximumPriority())
						.userCount(userSkillPriorityReport.getUserCount())
						.skillName(userSkillPriorityReport.getSkillReport().getName())
						.build())
				.orElseThrow(() -> new IllegalArgumentException(format("UserSkillPriorityReport with ID '%s' not found", reportId)));
	}
}
