package com.tsmms.skoop.skillgroup.command;

import com.tsmms.skoop.exception.DuplicateResourceException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.skillgroup.SkillGroup;
import com.tsmms.skoop.skillgroup.SkillGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static java.lang.String.format;

@Service
public class SkillGroupCommandService {
	private SkillGroupRepository skillGroupRepository;

	public SkillGroupCommandService(SkillGroupRepository skillGroupRepository) {
		this.skillGroupRepository = skillGroupRepository;
	}

	@Transactional
	public SkillGroup createGroup(String name, String description) {
		skillGroupRepository.findByNameIgnoreCase(name).ifPresent(skillGroup -> {
			throw DuplicateResourceException.builder()
					.message(format("Skill group with name '%s' already exists", name))
					.build();
		});
		return skillGroupRepository.save(SkillGroup.builder()
				.id(UUID.randomUUID().toString())
				.name(name)
				.description(description)
				.build());
	}

	@Transactional
	public SkillGroup updateGroup(String id, String name, String description) {
		SkillGroup skillGroup = skillGroupRepository.findById(id).orElseThrow(() -> {
			String[] searchParamsMap = {"id", id};
			return NoSuchResourceException.builder()
					.model(Model.SKILL_GROUP)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		skillGroup.setName(name);
		skillGroup.setDescription(description);
		return skillGroupRepository.save(skillGroup);
	}

	@Transactional
	public void deleteGroup(String id) {
		SkillGroup skillGroup = skillGroupRepository.findById(id).orElseThrow(() -> {
			String[] searchParamsMap = {"id", id};
			return NoSuchResourceException.builder()
					.model(Model.SKILL_GROUP)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		skillGroupRepository.delete(skillGroup);
	}
}
