package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.exception.EmptyInputException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityAggregationReportResult;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryService;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import io.knowledgeassets.myskills.server.userskill.UserSkillPriorityAggregationResult;
import io.knowledgeassets.myskills.server.userskill.UserSkillRepository;
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
	public Stream<UserSkill> getBySkillId(String skillId) throws BusinessException {
		return getBySkillId(skillId, 0);
	}

	@Transactional(readOnly = true)
	public Stream<UserSkill> getBySkillId(String skillId, Integer minPriority) throws BusinessException {
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
	public Stream<UserSkillPriorityAggregationReportResult> getAllUserSkillPriorityAggregationResults() {
		return StreamSupport.stream(userSkillRepository.findAllPrioritizedSkills().spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<Skill> getUserSkillSuggestions(String userId, String search) throws EmptyInputException, NoSuchResourceException {
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
