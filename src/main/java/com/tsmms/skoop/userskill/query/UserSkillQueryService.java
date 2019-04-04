package com.tsmms.skoop.userskill.query;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.query.SkillQueryService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.userskill.UserSkill;
import com.tsmms.skoop.userskill.UserSkillPriorityAggregationResult;
import com.tsmms.skoop.userskill.UserSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserSkillQueryService {
	private UserSkillRepository userSkillRepository;
	private UserQueryService userQueryService;
	private SkillQueryService skillQueryService;

	public UserSkillQueryService(UserSkillRepository userSkillRepository,
								 UserQueryService userQueryService,
								 SkillQueryService skillQueryService) {
		this.userSkillRepository = userSkillRepository;
		this.userQueryService = userQueryService;
		this.skillQueryService = skillQueryService;
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
	public Stream<UserSkill> getBySkillId(String skillId) {
		return getBySkillId(skillId, 0);
	}

	@Transactional(readOnly = true)
	public Stream<UserSkill> getBySkillId(String skillId, Integer minPriority) {
		if (!skillQueryService.exists(skillId)) {
			String[] searchParamsMap = {"id", skillId};
			throw NoSuchResourceException.builder()
					.model(Model.SKILL)
					.searchParamsMap(searchParamsMap)
					.build();
		}

		if (minPriority != null && minPriority > 0) {
			return StreamSupport.stream(userSkillRepository.findBySkillIdAndPriorityGreaterThanEqual(
					skillId, minPriority).spliterator(), false);
		} else {
			return StreamSupport.stream(userSkillRepository.findBySkillId(skillId).spliterator(), false);
		}
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillPriorityAggregationResult> getTop10UserSkillPriorityAggregationResults() {
		return StreamSupport.stream(userSkillRepository.findTop10PrioritizedSkills().spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<Skill> getUserSkillSuggestions(String userId, String search) {
		if (!userQueryService.exists(userId)) {
			String[] searchParamsMap = {"id", userId};
			throw NoSuchResourceException.builder()
					.model(Model.USER)
					.searchParamsMap(searchParamsMap)
					.build();
		}
		return StreamSupport.stream(userSkillRepository.findSkillSuggestionsByUserId(userId, search)
				.spliterator(), false);
	}
}
