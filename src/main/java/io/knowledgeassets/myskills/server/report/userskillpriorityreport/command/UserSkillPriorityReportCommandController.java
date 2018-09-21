package io.knowledgeassets.myskills.server.report.userskillpriorityreport.command;

import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReportResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Reports", description = "API allowing modifications of reports")
@RestController
public class UserSkillPriorityReportCommandController {
	private UserSkillPriorityReportCommandService userSkillPriorityReportCommandService;

	public UserSkillPriorityReportCommandController(UserSkillPriorityReportCommandService userSkillPriorityReportCommandService) {
		this.userSkillPriorityReportCommandService = userSkillPriorityReportCommandService;
	}

	@ApiOperation(value = "Create a new Report",
			notes = "Create a new report in the system.")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@PostMapping(path = "/reports",
			consumes = MediaType.ALL_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserSkillPriorityReportResponse> createReport() {
		UserSkillPriorityReport userSkillPriorityReport = userSkillPriorityReportCommandService.createPriorityReport();
		return ResponseEntity.status(HttpStatus.CREATED).body(UserSkillPriorityReportResponse.builder()
				.id(userSkillPriorityReport.getId())
				.date(userSkillPriorityReport.getDate())
				.skillCount(userSkillPriorityReport.getUserSkillPriorityAggregationReports().size())
				.build());
	}
}
