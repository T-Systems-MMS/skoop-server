package com.tsmms.skoop.skill.query;

import com.tsmms.skoop.exception.EmptyInputException;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Service
public class SkillQueryService {
	private SkillRepository skillRepository;

	public SkillQueryService(SkillRepository skillRepository) {
		this.skillRepository = skillRepository;
	}

	@Transactional(readOnly = true)
	public Stream<Skill> getSkills() {
		return StreamSupport.stream(skillRepository.findAll().spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Optional<Skill> getSkillById(String skillId) {
		return skillRepository.findById(skillId);
	}

	/**
	 * It ignores case sensitivity
	 *
	 * @param skillName
	 * @return
	 */
	@Transactional(readOnly = true)
	public Optional<Skill> findByNameIgnoreCase(String skillName) {
		return skillRepository.findByNameIgnoreCase(skillName);
	}

	@Transactional(readOnly = true)
	public boolean exists(String skillId) {
		if (skillId == null) {
			throw EmptyInputException.builder()
					.message("skillId is null.")
					.build();
		}
		return skillRepository.existsById(skillId);
	}

	public Boolean isSkillExist(String search) {
		return skillRepository.isSkillExistByNameIgnoreCase(search);
	}

	@Transactional(readOnly = true)
	public Stream<Skill> getSkillsByIds(List<String> skillIds) {
		return stream(skillRepository.findAllById(skillIds).spliterator(), false);
	}

	@Transactional(readOnly = true)
	public List<Skill> convertSkillNamesToSkills(List<String> skillNames) {
		final List<Skill> skills;
		if (skillNames != null) {
			skills = skillNames.stream().map(skillName ->
					findByNameIgnoreCase(skillName).orElse(
							Skill.builder()
									.name(skillName)
									.build()
					)).collect(toList());
		}
		else {
			skills = Collections.emptyList();
		}
		return skills;
	}

}
