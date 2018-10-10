package io.knowledgeassets.myskills.server.skill.query;

import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillResponse;
import io.knowledgeassets.myskills.server.skillgroup.SkillGroup;
import io.knowledgeassets.myskills.server.skillgroup.query.SkillGroupQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Api(tags = "Skills", description = "API allowing queries of skills")
@RestController
public class SkillQueryController {
	private SkillQueryService skillQueryService;

	public SkillQueryController(SkillQueryService skillQueryService) {
		this.skillQueryService = skillQueryService;
	}

	@ApiOperation(value = "Get all skills",
			notes = "Get all skills currently stored in the system. The list is unsorted.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/skills", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SkillResponse> getSkills() {
		return skillQueryService.getSkills()
				.map(skill -> SkillResponse.builder()
						.id(skill.getId())
						.name(skill.getName())
						.description(skill.getDescription())
						.skillGroups(getGroups(skill.getSkillGroups()))
						.build())
				.collect(toList());
	}

	@ApiOperation(value = "Get a specific skill",
			notes = "Get a specific skill currently stored in the system.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/skills/{skillId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public SkillResponse getSkill(@PathVariable("skillId") String skillId) {
		return skillQueryService.getSkillById(skillId)
				.map(skill -> SkillResponse.builder()
						.id(skill.getId())
						.name(skill.getName())
						.description(skill.getDescription())
						.skillGroups(getGroups(skill.getSkillGroups()))
						.build())
				.orElseThrow(() -> {
					String[] searchParamsMap = {"id", skillId};
					return NoSuchResourceException.builder()
							.model(Model.SKILL)
							.searchParamsMap(searchParamsMap)
							.build();
				});
	}

	@ApiOperation(value = "If a skill with the specific skillName existsReport it return true, otherwise false.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource, e.g. foreign user data"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(path = "/skills/skill-existence", produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean isSkillExist(@RequestParam("search") String skillName) throws BusinessException {
		return skillQueryService.isSkillExist(skillName);
	}

	private List<String> getGroups(List<SkillGroup> skillGroups) {
		if (!CollectionUtils.isEmpty(skillGroups)) {
			return skillGroups.stream().map(SkillGroup::getName).collect(toList());
		} else {
			return null;
		}
	}
}
