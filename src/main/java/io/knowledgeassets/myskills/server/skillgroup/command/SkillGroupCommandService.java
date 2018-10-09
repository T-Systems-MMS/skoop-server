package io.knowledgeassets.myskills.server.skillgroup.command;

import io.knowledgeassets.myskills.server.exception.DuplicateResourceException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.skillgroup.SkillGroup;
import io.knowledgeassets.myskills.server.skillgroup.SkillGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static java.lang.String.format;

@Service
public class SkillGroupCommandService {
	private SkillGroupRepository groupRepository;

	public SkillGroupCommandService(SkillGroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

	@Transactional
	public SkillGroup createGroup(String name, String description) {
		groupRepository.findByNameIgnoreCase(name).ifPresent(skillGroup -> {
			throw DuplicateResourceException.builder()
					.message(format("Skill group with name '%s' already exists", name))
					.build();
		});
		return groupRepository.save(SkillGroup.builder()
				.id(UUID.randomUUID().toString())
				.name(name)
				.description(description)
				.build());
	}

	@Transactional
	public SkillGroup updateGroup(String id, String name, String description) {
		SkillGroup skillGroup = groupRepository.findById(id).orElseThrow(() -> {
			String[] searchParamsMap = {"id", id};
			return NoSuchResourceException.builder()
					.model(Model.SKILL_GROUP)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		skillGroup.setName(name);
		skillGroup.setDescription(description);
		return groupRepository.save(skillGroup);
	}

	@Transactional
	public void deleteGroup(String id) {
		SkillGroup skillGroup = groupRepository.findById(id).orElseThrow(() -> {
			String[] searchParamsMap = {"id", id};
			return NoSuchResourceException.builder()
					.model(Model.SKILL_GROUP)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		groupRepository.delete(skillGroup);
	}
}
