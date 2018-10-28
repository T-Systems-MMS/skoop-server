package io.knowledgeassets.myskills.server.report.userskillpriority.query;

import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.report.skill.SkillReportSimpleResponse;
import io.knowledgeassets.myskills.server.report.user.UserReportSimpleResponse;
import io.knowledgeassets.myskills.server.report.userskill.UserSkillReportResponse;
import io.knowledgeassets.myskills.server.report.userskillpriority.*;
import io.knowledgeassets.myskills.server.security.UserIdentity;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.query.UserPermissionQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Set;

import static io.knowledgeassets.myskills.server.user.UserPermissionScope.READ_USER_SKILLS;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Api(tags = "Reports", description = "API allowing queries of user skill priority reports")
@RestController
public class UserSkillPriorityReportQueryController {
	private UserSkillPriorityReportQueryService userSkillPriorityReportQueryService;
	private UserPermissionQueryService userPermissionQueryService;

	public UserSkillPriorityReportQueryController(UserSkillPriorityReportQueryService userSkillPriorityReportQueryService,
												  UserPermissionQueryService userPermissionQueryService) {
		this.userSkillPriorityReportQueryService = userSkillPriorityReportQueryService;
		this.userPermissionQueryService = userPermissionQueryService;
	}

	@ApiOperation(
			value = "Get all user skill priority reports",
			notes = "Get all reports currently stored in the system. The list is sorted by date in descending order."
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/reports/skills/priority", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserSkillPriorityReportSimpleResponse> getReports() {
		return userSkillPriorityReportQueryService.getReports()
				.map(report -> UserSkillPriorityReportSimpleResponse.builder()
						.id(report.getId())
						.date(report.getDate())
						.skillCount(report.getAggregationReports().size())
						.build())
				.collect(toList());
	}

	@ApiOperation(value = "Get details of a specific report",
			notes = "Get details of a a specific report, currently stored in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/reports/skills/priority/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserSkillPriorityReportResponse getReportById(@PathVariable("reportId") String reportId) throws BusinessException {
		UserSkillPriorityReport report = userSkillPriorityReportQueryService.getReportById(reportId);
		return UserSkillPriorityReportResponse.builder()
				.id(report.getId())
				.date(report.getDate())
				.aggregationReports(convertToAggregationReportResponses(report.getAggregationReports()))
				.build();
	}

	private List<UserSkillPriorityAggregationReportResponse> convertToAggregationReportResponses(
			List<UserSkillPriorityAggregationReport> aggregationReports) {
		// TODO: Sort reports by average priority in descending order.
		return aggregationReports.stream()
				.map(report -> UserSkillPriorityAggregationReportResponse.builder()
						.id(report.getId())
						.averagePriority(report.getAveragePriority())
						.maximumPriority(report.getMaximumPriority())
						.userCount(report.getUserCount())
						.skill(SkillReportSimpleResponse.builder()
								.name(report.getSkillName())
								.description(report.getSkillDescription())
								.build())
						.build())
				.collect(toList());
	}

	@ApiOperation(
			value = "Get all users for a specific skill inside a user skill priority report",
			notes = "Get the list of users along with user skill information."
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(
			path = "/reports/skills/priority/{reportId}/aggregations/{aggregationReportId}/users",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public List<UserSkillReportResponse> getUserSkillReportsByAggregationReportId(
			@PathVariable("aggregationReportId") String aggregationReportId,
			@ApiIgnore @AuthenticationPrincipal UserIdentity userIdentity) throws BusinessException {
		// Create a whitelist of those user names who allowed the principal read access to their skill relationships.
		Set<String> allowedUserNames = userPermissionQueryService.getUsersWhoGrantedPermission(
				userIdentity.getUserId(), READ_USER_SKILLS).map(User::getUserName).collect(toSet());
		allowedUserNames.add(userIdentity.getUserName());

		return userSkillPriorityReportQueryService.getUserSkillReportsByAggregationReportId(aggregationReportId)
				.filter(userSkillReport -> allowedUserNames.contains(userSkillReport.getUserName()))
				.map(report -> UserSkillReportResponse.builder()
						.id(report.getId())
						.currentLevel(report.getCurrentLevel())
						.desiredLevel(report.getDesiredLevel())
						.priority(report.getPriority())
						.skill(SkillReportSimpleResponse.builder()
								.name(report.getSkillName())
								.description(report.getSkillDescription())
								.build())
						.user(UserReportSimpleResponse.builder()
								.userName(report.getUserName())
								.firstName(report.getUserFirstName())
								.lastName(report.getUserLastName())
								.build())
						.build())
				.collect(toList());
	}
}
