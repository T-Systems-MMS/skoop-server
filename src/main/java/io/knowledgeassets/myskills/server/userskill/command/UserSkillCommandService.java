package io.knowledgeassets.myskills.server.userskill.command;

import io.knowledgeassets.myskills.server.exception.DuplicateResourceException;
import io.knowledgeassets.myskills.server.exception.InvalidInputException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.command.SkillCommandService;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryService;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import io.knowledgeassets.myskills.server.userskill.UserSkillRepository;
import org.springframework.security.access.prepost.PreAuthorize;
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
	// TODO: Use a command object instead of multiple method parameters.
	@Transactional
	public UserSkill createUserSkillBySkillId(String userId, String skillId, Integer currentLevel, Integer desiredLevel,
											  Integer priority) {
		userSkillRepository.findByUserIdAndSkillId(userId, skillId).ifPresent(userSkill -> {
			throw DuplicateResourceException.builder()
					.message(format("User with ID '%s' is already related to skill with ID '%s'", userId, skillId))
					.build();
		});
		User user = userQueryService.getUserById(userId).orElseThrow(() -> {
			String[] searchParamsMap = {"id", userId};
			return NoSuchResourceException.builder()
					.model(Model.USER)
					.searchParamsMap(searchParamsMap)
					.build();
		});
		Skill skill = skillQueryService.getSkillById(skillId).orElseThrow(() -> {
			String[] searchParamsMap = {"id", skillId};
			return NoSuchResourceException.builder()
					.model(Model.SKILL)
					.searchParamsMap(searchParamsMap)
					.build();
		});
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
		Skill skill = skillQueryService.findByNameIgnoreCase(skillName)
				.orElseGet(() -> skillCommandService.createSkill(skillName, null, null));
		return createUserSkillBySkillId(userId, skill.getId(), currentLevel, desiredLevel, priority);
	}

	/**
	 * This authentication.userAuthentication.Principal returns the UserIdentity object.
	 * This makes sure that only owner or Admin users can change the userSkill record.
	 *
	 * @PreAuthorize("#userSkill.user.id.equals(authentication.userAuthentication.Principal.userId)")
	 * @PreAuthorize("#userSkill.user.userName.equals(authentication.userAuthentication.Principal.userName)")
	 * @PreAuthorize("#userSkill.user.userName == authentication.name")
	 */
	@Transactional
	@PreAuthorize("#userSkill.user.userName.equals(authentication.userAuthentication.Principal.userName) || hasRole('ADMIN')")
	public UserSkill updateUserSkill(Integer currentLevel, Integer desiredLevel, Integer priority, UserSkill userSkill) {
		userSkill.setCurrentLevel(currentLevel);
		userSkill.setDesiredLevel(desiredLevel);
		userSkill.setPriority(priority);
		return userSkillRepository.save(userSkill);
	}

	/**
	 * This makes sure that only owner or Admin users can delete the userSkill record.
	 *
	 * @param userSkill
	 */
	@Transactional
	@PreAuthorize("#userSkill.user.userName.equals(authentication.userAuthentication.Principal.userName) || hasRole('ADMIN')")
	public void deleteUserSkill(UserSkill userSkill) {
		userSkillRepository.delete(userSkill);
	}

	@Transactional(readOnly = true)
	public UserSkill getUserSkill(String userId, String skillId) {
		return userSkillRepository.findByUserIdAndSkillId(userId, skillId)
				.orElseThrow(() -> InvalidInputException.builder().message(
						format("User with ID '%s' is not related to skill with ID '%s'", userId, skillId)).build());
	}
}
