package io.knowledgeassets.myskills.server.skill.command;

import io.knowledgeassets.myskills.server.exception.DuplicateResourceException;
import io.knowledgeassets.myskills.server.exception.EmptyInputException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static java.lang.String.format;

@Service
public class SkillCommandService {
	private SkillRepository skillRepository;

	public SkillCommandService(SkillRepository skillRepository) {
		this.skillRepository = skillRepository;
	}

	@Transactional
	public Skill createSkill(String name, String description) {
		skillRepository.findByNameIgnoreCase(name).ifPresent(skill -> {
			throw DuplicateResourceException.builder()
					.message(format("Skill with name '%s' already exists", name))
					.build();
		});
		return skillRepository.save(Skill.builder()
				.id(UUID.randomUUID().toString())
				.name(name)
				.description(description)
				.build());
	}

	@Transactional
	public Skill updateSkill(String id, String name, String description) {
		Skill skill = skillRepository.findById(id).orElseThrow(() -> {
			String[] searchParamsMap = {"id", id};
			return NoSuchResourceException.builder()
					.model(Model.SKILL)
					.searchParamsMap(searchParamsMap)
					.build();
		});
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
