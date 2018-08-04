package io.knowledgeassets.myskills.server.skill.command;

import io.knowledgeassets.myskills.server.common.AbstractDelegateRepository;
import io.knowledgeassets.myskills.server.skill.query.Skill;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryRepository;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.springframework.stereotype.Repository;

@Repository
public class SkillAggregateRepository extends AbstractDelegateRepository<SkillAggregate, Skill> {
	public SkillAggregateRepository(EventBus eventBus, ParameterResolverFactory parameterResolverFactory,
									SkillQueryRepository delegateRepository) {
		super(SkillAggregate.class, eventBus, parameterResolverFactory, delegateRepository);
	}

	@Override
	protected Skill convertToEntity(SkillAggregate aggregate) {
		return new Skill()
				.id(aggregate.getId())
				.name(aggregate.getName())
				.description(aggregate.getDescription());
	}

	@Override
	protected SkillAggregate convertToAggregate(Skill entity) {
		return new SkillAggregate()
				.id(entity.getId())
				.name(entity.getName())
				.description(entity.getDescription());
	}
}
