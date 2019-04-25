package com.tsmms.skoop.skill.query;

import com.tsmms.skoop.exception.EmptyInputException;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.StreamSupport.stream;
import static java.util.stream.Collectors.toCollection;

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

	private <C extends Collection<Skill>> Optional<C> convertSkillNamesToSkills(Supplier<? extends C> collectionFactory, Collection<String> skillNames) {
		if (skillNames != null) {
			return Optional.of(skillNames.stream().map(skillName ->
					findByNameIgnoreCase(skillName).orElse(
							Skill.builder()
									.name(skillName)
									.build()
					)).collect(toCollection(collectionFactory)));
		}
		else {
			return Optional.empty();
		}
	}

	@Transactional(readOnly = true)
	public List<Skill> convertSkillNamesToSkillsList(Collection<String> skillNames) {
		return this.<List<Skill>>convertSkillNamesToSkills(ArrayList::new, skillNames).orElse(Collections.emptyList());
	}

	@Transactional(readOnly = true)
	public Set<Skill> convertSkillNamesToSkillsSet(Collection<String> skillNames) {
		return this.<Set<Skill>>convertSkillNamesToSkills(HashSet::new, skillNames).orElse(Collections.emptySet());
	}

}
