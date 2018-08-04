package io.knowledgeassets.myskills.server.userskill.command;

import io.knowledgeassets.myskills.server.common.AbstractDelegateRepository;
import io.knowledgeassets.myskills.server.skill.query.Skill;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryRepository;
import io.knowledgeassets.myskills.server.user.query.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryRepository;
import io.knowledgeassets.myskills.server.userskill.command.UserSkillAggregate.UserSkillAggregateKey;
import io.knowledgeassets.myskills.server.userskill.query.UserSkill;
import io.knowledgeassets.myskills.server.userskill.query.UserSkillQueryRepository;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.springframework.stereotype.Repository;

import static java.lang.String.format;

@Repository
public class UserSkillAggregateRepository extends AbstractDelegateRepository<UserSkillAggregate, UserSkill> {
	private UserQueryRepository userQueryRepository;
	private SkillQueryRepository skillQueryRepository;

	public UserSkillAggregateRepository(EventBus eventBus, ParameterResolverFactory parameterResolverFactory,
										UserSkillQueryRepository delegateRepository,
										UserQueryRepository userQueryRepository,
										SkillQueryRepository skillQueryRepository) {
		super(UserSkillAggregate.class, eventBus, parameterResolverFactory, delegateRepository);
		this.userQueryRepository = userQueryRepository;
		this.skillQueryRepository = skillQueryRepository;
	}

	@Override
	protected UserSkill convertToEntity(UserSkillAggregate aggregate) {
		// Fetch the possibly existing relationship.
		UserSkill userSkill = delegateRepository.findById(aggregate.getId().toString()).orElse(null);
		if (userSkill == null) {
			// Fetch the user to assign the skill to.
			User user = userQueryRepository.findById(aggregate.getId().getUserId())
					.orElseThrow(() -> new IllegalArgumentException(format("User with ID '%s' not found",
							aggregate.getId().getUserId())));

			// Fetch the skill to be assigned to the user.
			Skill skill = skillQueryRepository.findById(aggregate.getId().getSkillId())
					.orElseThrow(() -> new IllegalArgumentException(format("Skill with ID '%s' not found",
							aggregate.getId().getSkillId())));

			// Create new relationship between given user and skill.
			userSkill = new UserSkill().id(aggregate.getId().toString()).user(user).skill(skill);
		}

		// Set the given levels and priority for the relationship.
		userSkill.currentLevel(aggregate.getCurrentLevel())
				.desiredLevel(aggregate.getDesiredLevel())
				.priority(aggregate.getPriority());
		return userSkill;
	}

	@Override
	protected UserSkillAggregate convertToAggregate(UserSkill entity) {
		return new UserSkillAggregate()
				.id(UserSkillAggregateKey.fromString(entity.getId()))
				.currentLevel(entity.getCurrentLevel())
				.desiredLevel(entity.getDesiredLevel())
				.priority(entity.getPriority());
	}
}
