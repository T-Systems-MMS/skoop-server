package io.knowledgeassets.myskills.server.userskill.command;

import io.knowledgeassets.myskills.server.skill.command.SkillCommandService;
import io.knowledgeassets.myskills.server.skill.query.Skill;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryService;
import io.knowledgeassets.myskills.server.userskill.command.UserSkillAggregate.UserSkillAggregateKey;
import io.knowledgeassets.myskills.server.userskill.query.UserSkill;
import io.knowledgeassets.myskills.server.userskill.query.UserSkillQueryService;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
public class UserSkillCommandService {
	private CommandGateway commandGateway;
	private SkillQueryService skillQueryService;
	private SkillCommandService skillCommandService;
	private UserSkillQueryService userSkillQueryService;

	public UserSkillCommandService(CommandGateway commandGateway, SkillQueryService skillQueryService,
								   SkillCommandService skillCommandService, UserSkillQueryService userSkillQueryService) {
		this.commandGateway = commandGateway;
		this.skillQueryService = skillQueryService;
		this.skillCommandService = skillCommandService;
		this.userSkillQueryService = userSkillQueryService;
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
		commandGateway.sendAndWait(new CreateUserSkillCommand(userId, skillId, currentLevel, desiredLevel, priority));
		return userSkillQueryService.getUserSkillByUserIdAndSkillId(userId, skillId)
				.orElseThrow(() -> new IllegalStateException(
						format("User with ID '%s' not related to skill with ID '%s'", userId, skillId)));
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
		Skill skill = skillQueryService.getSkillByName(skillName)
				.orElseGet(() -> skillCommandService.createSkill(skillName, null));
		return createUserSkillBySkillId(userId, skill.getId(), currentLevel, desiredLevel, priority);
	}

	@Transactional
	public UserSkill updateUserSkill(String userId, String skillId, Integer currentLevel, Integer desiredLevel,
									 Integer priority) {
		commandGateway.sendAndWait(new UpdateUserSkillCommand(new UserSkillAggregateKey(userId, skillId), currentLevel,
				desiredLevel, priority));
		return userSkillQueryService.getUserSkillByUserIdAndSkillId(userId, skillId)
				.orElseThrow(() -> new IllegalStateException(
						format("User with ID '%s' not related to skill with ID '%s'", userId, skillId)));
	}

	@Transactional
	public void deleteUserSkill(String userId, String skillId) {
		commandGateway.sendAndWait(new DeleteUserSkillCommand(new UserSkillAggregateKey(userId, skillId)));
	}
}
