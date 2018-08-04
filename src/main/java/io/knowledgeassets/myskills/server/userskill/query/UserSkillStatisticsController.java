package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.skill.SkillResponse;
import io.knowledgeassets.myskills.server.userskill.UserSkillPriorityAggregationResponse;
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

@Api(tags = "Statistics", description = "API allowing queries of statistics for relationships between users and skills")
@RestController
public class UserSkillStatisticsController {
	private UserSkillQueryService userSkillQueryService;

	public UserSkillStatisticsController(UserSkillQueryService userSkillQueryService) {
		this.userSkillQueryService = userSkillQueryService;
	}

	@ApiOperation(value = "Get top 10 prioritized skills",
			notes = "Get the top 10 of those skills with the highest average priority based on user relationships. " +
					"The list is sorted by average priority in descending order.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/statistics/skills/top-priority", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserSkillPriorityAggregationResponse> getTop10PrioritizedSkills() {
		return userSkillQueryService.getTop10PrioritizedSkills()
				.map(aggregation -> new UserSkillPriorityAggregationResponse()
						.skill(new SkillResponse()
								.id(aggregation.getSkill().getId())
								.name(aggregation.getSkill().getName())
								.description(aggregation.getSkill().getDescription()))
						.averagePriority(aggregation.getAveragePriority())
						.maximumPriority(aggregation.getMaximumPriority())
						.userCount(aggregation.getUserCount()))
				.collect(toList());
	}
}
