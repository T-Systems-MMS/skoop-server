package io.knowledgeassets.myskills.server.search.query;

import io.knowledgeassets.myskills.server.search.AnonymousUserSkillResult;
import io.knowledgeassets.myskills.server.search.UserSearchSkillCriterion;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Api(tags = "Search", description = "API to search for anonymous user skills.")
@RestController
@Validated
@RequestMapping(path = "/search")
public class AnonymousUserSkillSearchController {

	private final AnonymousUserSkillSearchService anonymousUserSkillSearchService;

	public AnonymousUserSkillSearchController(AnonymousUserSkillSearchService anonymousUserSkillSearchService) {
		this.anonymousUserSkillSearchService = requireNonNull(anonymousUserSkillSearchService);
	}

	@ApiOperation(
			value = "Get anonymous user skills at least at the specified level.",
			notes = "Get anonymous user skills at least at the specified level."
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path = "/skills", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AnonymousUserSkillResponse>> searchForAnonymousUserSkills(
			@Valid @NotEmpty @RequestParam("params") List<@Pattern(regexp = "^.+\\+\\d+$") String> searchParameters) {
		final List<UserSearchSkillCriterion> searchParams = extractUserSearchSkillCriterionList(searchParameters);
		final List<AnonymousUserSkillResult> results = anonymousUserSkillSearchService.findBySkillLevel(searchParams).collect(Collectors.toList());
		return ResponseEntity.ok().body(results.stream().map((AnonymousUserSkillResult r) ->
				AnonymousUserSkillResponse.builder()
						.userReferenceId(r.getReferenceId())
						.skills(r.getUserSkills().stream()
								.map((UserSkill us) -> CurrentSkillLevelResponse.builder()
										.currentLevel(us.getCurrentLevel())
										.skillName(us.getSkill().getName())
										.build())
								.collect(Collectors.toList()))
						.build()
		).collect(Collectors.toList()));
	}

	private List<UserSearchSkillCriterion> extractUserSearchSkillCriterionList(List<String> searchParameters) {
		return searchParameters.stream().map(s -> {
			final String[] sp = s.split("\\+");
			return UserSearchSkillCriterion.builder()
					.skillId(sp[0])
					.minimumCurrentLevel(Integer.valueOf(sp[1]))
					.build();
		}).collect(Collectors.toList());
	}

}
