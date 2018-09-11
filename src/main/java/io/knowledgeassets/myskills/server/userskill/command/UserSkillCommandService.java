package io.knowledgeassets.myskills.server.userskill.command;

import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.command.SkillCommandService;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryService;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import io.knowledgeassets.myskills.server.userskill.UserSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
public class UserSkillCommandService {
	private UserQueryService userQueryService;
	private SkillQueryService skillQueryService;
	private SkillCommandService skillCommandService;
	private UserSkillRepository userSkillRepository;

	public UserSkillCommandService(UserQueryService userQueryService, SkillQueryService skillQueryService,
								   SkillCommandService skillCommandService, UserSkillRepository userSkillRepository) {
		this.userQueryService = userQueryService;
		this.skillQueryService = skillQueryService;
		this.skillCommandService = skillCommandService;
		this.userSkillRepository = userSkillRepository;
	}

	/**
	 * Relates the existing user with the given ID to the existing skill with the given ID.
	 *
	 * @param userId       ID of the existing user.
	 * @param skillId      ID of the existing skill.
	 * @param currentLevel Current skill level of the user.
	 * @param desiredLevel Desired skill level of the user.
	 * @param priority     Priority to reach the desired skill level.
	 * @return Newly created user skill relationship.
	 */
	@Transactional
	public UserSkill createUserSkillBySkillId(String userId, String skillId, Integer currentLevel, Integer desiredLevel,
											  Integer priority) {
		userSkillRepository.findByUserIdAndSkillId(userId, skillId).ifPresent(userSkill -> {
			throw new IllegalArgumentException(format("User with ID '%s' is already related to skill with ID '%s'",
					userId, skillId));
		});
		User user = userQueryService.getUserById(userId).orElseThrow(() -> new IllegalArgumentException(
				format("User with ID '%s' not found", userId)));
		Skill skill = skillQueryService.getSkillById(skillId).orElseThrow(() -> new IllegalArgumentException(
				format("Skill with ID '%s' not found", skillId)));
		return userSkillRepository.save(UserSkill.builder()
				.id(userId + ';' + skillId)
				.user(user)
				.skill(skill)
				.currentLevel(currentLevel)
				.desiredLevel(desiredLevel)
				.priority(priority)
				.build());
	}

	/**
	 * Relates the existing user with the given ID to the skill with the given name. If the skill name already exists
	 * the user is related to the existing skill, otherwise a new skill with the given name is created.
	 *
	 * @param userId       ID of the existing user.
	 * @param skillName    Name of the skill.
	 * @param currentLevel Current skill level of the user.
	 * @param desiredLevel Desired skill level of the user.
	 * @param priority     Priority to reach the desired skill level.
	 * @return Newly created user skill relationship.
	 */
	@Transactional
	public UserSkill createUserSkillBySkillName(String userId, String skillName, Integer currentLevel,
												Integer desiredLevel, Integer priority) {
		Skill skill = skillQueryService.getByName(skillName)
				.orElseGet(() -> skillCommandService.createSkill(skillName, null));
		return createUserSkillBySkillId(userId, skill.getId(), currentLevel, desiredLevel, priority);
	}

	@Transactional
	public UserSkill updateUserSkill(String userId, String skillId, Integer currentLevel, Integer desiredLevel,
									 Integer priority) {
		UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(userId, skillId)
				.orElseThrow(() -> new IllegalArgumentException(
						format("User with ID '%s' is not related to skill with ID '%s'", userId, skillId)));
		userSkill.setCurrentLevel(currentLevel);
		userSkill.setDesiredLevel(desiredLevel);
		userSkill.setPriority(priority);
		return userSkillRepository.save(userSkill);
	}

	@Transactional
	public void deleteUserSkill(String userId, String skillId) {
		UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(userId, skillId)
				.orElseThrow(() -> new IllegalArgumentException(
						format("User with ID '%s' is not related to skill with ID '%s'", userId, skillId)));
		userSkillRepository.delete(userSkill);
	}
}
