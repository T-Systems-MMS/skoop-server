package io.knowledgeassets.myskills.server.skill.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class SkillQueryService {
	private SkillQueryRepository skillQueryRepository;

	public SkillQueryService(SkillQueryRepository skillQueryRepository) {
		this.skillQueryRepository = skillQueryRepository;
	}

	@Transactional(readOnly = true)
	public Stream<Skill> getSkills() {
		return StreamSupport.stream(skillQueryRepository.findAll().spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Optional<Skill> getSkillById(String skillId) {
		return skillQueryRepository.findById(skillId);
	}

	@Transactional(readOnly = true)
	public Optional<Skill> getSkillByName(String skillName) {
		return skillQueryRepository.findByName(skillName);
	}
}
