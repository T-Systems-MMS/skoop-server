package io.knowledgeassets.myskills.server.report.userskillreport.query;

import io.knowledgeassets.myskills.server.exception.BusinessException;
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

@Api(tags = "Reports", description = "API allowing queries of user skill report")
@RestController
public class UserSkillReportQueryController {
	private UserSkillReportQueryService userSkillReportQueryService;

	public UserSkillReportQueryController(UserSkillReportQueryService userSkillReportQueryService) {
		this.userSkillReportQueryService = userSkillReportQueryService;
	}

	@ApiOperation(value = "Get all users for a specific report skill",
			notes = "Get the list of users along with user skill information.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/reports/{userSkillPriorityAggregationReportId}/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SkillUserResponse> getUsersByUserSkillPriorityAggregationReportId(
			@PathVariable("userSkillPriorityAggregationReportId") String userSkillPriorityAggregationReportId) throws BusinessException {
		return userSkillReportQueryService.getUsersByUserSkillPriorityAggregationReportId(userSkillPriorityAggregationReportId)
				.map(userSkill -> SkillUserResponse.builder()
						.user(UserResponse.builder()
								.userName(userSkill.getUserName())
								.build())
						.currentLevel(userSkill.getCurrentLevel())
						.desiredLevel(userSkill.getDesiredLevel())
						.priority(userSkill.getPriority())
						.build())
				.collect(toList());
	}

}
