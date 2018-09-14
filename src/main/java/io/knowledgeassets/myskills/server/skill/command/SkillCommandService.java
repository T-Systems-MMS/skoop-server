package io.knowledgeassets.myskills.server.skill.command;

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
			throw new IllegalArgumentException(format("Skill with name '%s' already exists", name));
		});
		return skillRepository.save(Skill.builder()
				.id(UUID.randomUUID().toString())
				.name(name)
				.description(description)
				.build());
	}

	@Transactional
	public Skill updateSkill(String id, String name, String description) {
		Skill skill = skillRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
				format("Skill with ID '%s' not found", id)));
		skill.setName(name);
		skill.setDescription(description);
		return skillRepository.save(skill);
	}

	@Transactional
	public void deleteSkill(String id) {
		Skill skill = skillRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
				format("Skill with ID '%s' not found", id)));
		skillRepository.delete(skill);
	}
}
