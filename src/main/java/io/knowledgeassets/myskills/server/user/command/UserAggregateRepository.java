package io.knowledgeassets.myskills.server.user.command;

import io.knowledgeassets.myskills.server.common.AbstractDelegateRepository;
import io.knowledgeassets.myskills.server.user.query.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryRepository;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserAggregateRepository extends AbstractDelegateRepository<UserAggregate, User> {
	public UserAggregateRepository(EventBus eventBus, ParameterResolverFactory parameterResolverFactory,
								   UserQueryRepository delegateRepository) {
		super(UserAggregate.class, eventBus, parameterResolverFactory, delegateRepository);
	}

	@Override
	protected User convertToEntity(UserAggregate aggregate) {
		return new User()
				.id(aggregate.getId())
				.userName(aggregate.getUserName())
				.firstName(aggregate.getFirstName())
				.lastName(aggregate.getLastName())
				.email(aggregate.getEmail());
	}

	@Override
	protected UserAggregate convertToAggregate(User entity) {
		return new UserAggregate()
				.id(entity.getId())
				.userName(entity.getUserName())
				.firstName(entity.getFirstName())
				.lastName(entity.getLastName())
				.email(entity.getEmail());
	}
}
