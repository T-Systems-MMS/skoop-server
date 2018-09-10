package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.report.UserSkillPriorityAggregationReportResult;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import io.knowledgeassets.myskills.server.userskill.UserSkillPriorityAggregation;
import io.knowledgeassets.myskills.server.userskill.UserSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserSkillQueryService {
	private UserSkillRepository userSkillRepository;

	public UserSkillQueryService(UserSkillRepository userSkillRepository) {
		this.userSkillRepository = userSkillRepository;
	}

	@Transactional(readOnly = true)
	public Stream<UserSkill> getUserSkillsByUserId(String userId) {
		return StreamSupport.stream(userSkillRepository.findByUserId(userId).spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Optional<UserSkill> getUserSkillByUserIdAndSkillId(String userId, String skillId) {
		return userSkillRepository.findByUserIdAndSkillId(userId, skillId);
	}

	@Transactional(readOnly = true)
	public Stream<User> getCoachesByUserIdAndSkillId(String userId, String skillId) {
		return StreamSupport.stream(userSkillRepository.findCoachesByUserIdAndSkillId(userId, skillId)
				.spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<UserSkill> getUserSkillsBySkillId(String skillId) {
		return getUserSkillsBySkillId(skillId, 0);
	}

	@Transactional(readOnly = true)
	public Stream<UserSkill> getUserSkillsBySkillId(String skillId, Integer minPriority) {
		if (minPriority != null && minPriority > 0) {
			return StreamSupport.stream(userSkillRepository.findBySkillIdAndPriorityGreaterThanEqual(
					skillId, minPriority).spliterator(), false);
		} else {
			return StreamSupport.stream(userSkillRepository.findBySkillId(skillId).spliterator(), false);
		}
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillPriorityAggregation> getTop10PrioritizedSkills() {
		return StreamSupport.stream(userSkillRepository.findTop10PrioritizedSkills().spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillPriorityAggregationReportResult> findPrioritizedSkillsToCreateReport() {
		return StreamSupport.stream(userSkillRepository.findPrioritizedSkillsToCreateReport().spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<Skill> getUserSkillSuggestions(String userId, String search) {
		return StreamSupport.stream(userSkillRepository.findSkillSuggestionsByUserId(userId, search)
				.spliterator(), false);
	}
}
