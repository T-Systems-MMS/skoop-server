package com.tsmms.skoop.userskill.query;

import com.tsmms.skoop.skill.SkillResponse;
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

@Api(tags = "Statistics")
@RestController
public class UserSkillStatisticsController {
	private UserSkillQueryService userSkillQueryService;

	public UserSkillStatisticsController(UserSkillQueryService userSkillQueryService) {
		this.userSkillQueryService = userSkillQueryService;
	}

	@ApiOperation(
			value = "Get top 10 prioritized skills",
			notes = "Get the top 10 of those skills with the highest average priority based on user relationships. " +
					"The list is sorted by average priority in descending order."
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/statistics/skills/top-priority", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserSkillPriorityAggregationResponse> getTop10PrioritizedSkills() {
		return userSkillQueryService.getTop10UserSkillPriorityAggregationResults()
				.map(aggregationResult -> UserSkillPriorityAggregationResponse.builder()
						.skill(SkillResponse.of(aggregationResult.getSkill()))
						.averagePriority(aggregationResult.getAveragePriority())
						.maximumPriority(aggregationResult.getMaximumPriority())
						.userCount(aggregationResult.getUserCount())
						.build())
				.collect(toList());
	}
}
