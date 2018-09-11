package io.knowledgeassets.myskills.server.skill.query;

import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

	@Transactional(readOnly = true)
	public Optional<Skill> getByName(String skillName) {
		return skillRepository.findByName(skillName);
	}

	@Transactional(readOnly = true)
	public Optional<Skill> getById(String skillId) {
		return skillRepository.findById(skillId);
	}
}
