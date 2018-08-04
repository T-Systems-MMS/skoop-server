package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.user.query.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserSkillQueryService {
	private UserSkillQueryRepository userSkillQueryRepository;

	public UserSkillQueryService(UserSkillQueryRepository userSkillQueryRepository) {
		this.userSkillQueryRepository = userSkillQueryRepository;
	}

	@Transactional(readOnly = true)
	public Stream<UserSkill> getUserSkillsByUserId(String userId) {
		return StreamSupport.stream(userSkillQueryRepository.findByUserId(userId).spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Optional<UserSkill> getUserSkillByUserIdAndSkillId(String userId, String skillId) {
		return userSkillQueryRepository.findByUserIdAndSkillId(userId, skillId);
	}

	@Transactional(readOnly = true)
	public Stream<User> getCoachesByUserIdAndSkillId(String userId, String skillId) {
		return StreamSupport.stream(userSkillQueryRepository.findCoachesByUserIdAndSkillId(userId, skillId)
				.spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<UserSkill> getUserSkillsBySkillId(String skillId) {
		return getUserSkillsBySkillId(skillId, 0);
	}

	@Transactional(readOnly = true)
	public Stream<UserSkill> getUserSkillsBySkillId(String skillId, Integer minPriority) {
		if (minPriority != null && minPriority > 0) {
			return StreamSupport.stream(userSkillQueryRepository.findBySkillIdAndPriorityGreaterThanEqual(
					skillId, minPriority).spliterator(), false);
		} else {
			return StreamSupport.stream(userSkillQueryRepository.findBySkillId(skillId).spliterator(), false);
		}
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillPriorityAggregation> getTop10PrioritizedSkills() {
		return StreamSupport.stream(userSkillQueryRepository.findTop10PrioritizedSkills().spliterator(), false);
	}
}
