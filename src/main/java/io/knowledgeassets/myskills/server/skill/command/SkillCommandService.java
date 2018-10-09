package io.knowledgeassets.myskills.server.skill.command;

import io.knowledgeassets.myskills.server.exception.DuplicateResourceException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.skillgroup.SkillGroup;
import io.knowledgeassets.myskills.server.skillgroup.query.SkillGroupQueryService;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillRepository;
import io.knowledgeassets.myskills.server.skillgroup.command.SkillGroupCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Service
public class SkillCommandService {
	private SkillRepository skillRepository;
	private SkillGroupQueryService skillGroupQueryService;

	public SkillCommandService(SkillRepository skillRepository,
							   SkillGroupQueryService skillGroupQueryService) {
		this.skillRepository = skillRepository;
		this.skillGroupQueryService = skillGroupQueryService;
	}

	@Transactional
	public Skill createSkill(String name, String description, List<String> groups) {
		skillRepository.findByNameIgnoreCase(name).ifPresent(skill -> {
			throw DuplicateResourceException.builder()
					.message(format("Skill with name '%s' already existsReport", name))
					.build();
		});

		List<SkillGroup> skillGroups = new ArrayList<>();
		if (!CollectionUtils.isEmpty(groups)) {
			groups.forEach(groupName -> {
				SkillGroup skillGroup = skillGroupQueryService.findByNameIgnoreCase(groupName)
						.orElseThrow(() -> {
							String[] searchParamsMap = {"name", groupName};
							return NoSuchResourceException.builder()
									.model(Model.SKILL_GROUP)
									.searchParamsMap(searchParamsMap)
									.build();
						});
				skillGroups.add(skillGroup);
			});
		}
		Skill skill = skillRepository.save(Skill.builder()
				.id(UUID.randomUUID().toString())
				.name(name)
				.description(description)
				.skillGroups(skillGroups)
				.build());
		return skill;
	}

	@Transactional
	public Skill updateSkill(String id, String name, String description, List<String> groups) {
		Skill skill = skillRepository.findById(id).orElseThrow(() -> {
			String[] searchParamsMap = {"id", id};
			return NoSuchResourceException.builder()
					.model(Model.SKILL)
					.searchParamsMap(searchParamsMap)
					.build();
		});

		List<SkillGroup> skillGroups = new ArrayList<>();
		if (!CollectionUtils.isEmpty(groups)) {
			groups.forEach(groupName -> {
				SkillGroup skillGroup = skillGroupQueryService.findByNameIgnoreCase(groupName)
						.orElseThrow(() -> {
							String[] searchParamsMap = {"name", groupName};
							return NoSuchResourceException.builder()
									.model(Model.SKILL_GROUP)
									.searchParamsMap(searchParamsMap)
									.build();
						});
				skillGroups.add(skillGroup);
			});
		}
		skill.setSkillGroups(skillGroups);
		skill.setName(name);
		skill.setDescription(description);

		return skillRepository.save(skill);
	}

	@Transactional
	public void deleteSkill(String id) {
		Skill skill = skillRepository.findById(id).orElseThrow(() -> {
			String[] searchParamsMap = {"id", id};
			return NoSuchResourceException.builder()
					.model(Model.SKILL)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		skillRepository.delete(skill);
	}
}
