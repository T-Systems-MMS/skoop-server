package io.knowledgeassets.myskills.server.common;

import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.commandhandling.model.LockingRepository;
import org.axonframework.commandhandling.model.inspection.AnnotatedAggregate;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.concurrent.Callable;

import static java.lang.String.format;

/**
 * Abstract repository implementation to be used with the Axon Framework for persisting the current aggregate states in
 * a Spring Data {@link CrudRepository}.
 * <p>
 * Important: The {@link org.springframework.data.annotation.Id} type of the entity must be {@link String}. The delegate
 * repository will use the aggregate identifier as the entity Id when loading the state from the underlying repository.
 * </p>
 *
 * @param <A> Type of the aggregate provided by the repository. Must be a class annotated with {@link
 *            org.axonframework.spring.stereotype.Aggregate}.
 * @param <E> Type of the Spring Data entity used to store the aggregate state. Must be the entity type used on the
 *            given Spring Data repository.
 */
public abstract class AbstractDelegateRepository<A, E> extends LockingRepository<A, AnnotatedAggregate<A>> {
	protected EventBus eventBus;
	protected CrudRepository<E, String> delegateRepository;

	/**
	 * Instantiates a delegate repository with the underlying event bus and Spring Data repository.
	 *
	 * @param aggregateType            Type of the aggregate provided by the repository. Same class as the type
	 *                                 parameter on the class.
	 * @param eventBus                 Event bus implementation to provide to aggregate instances for event
	 *                                 distribution. Should be autowired in a Spring application.
	 * @param parameterResolverFactory Factory resolving method parameters for command/event handler methods. Should be
	 *                                 autowired in a Spring application.
	 * @param delegateRepository       Spring Data repository to delegate any data access to.
	 */
	protected AbstractDelegateRepository(
			Class<A> aggregateType, EventBus eventBus, ParameterResolverFactory parameterResolverFactory,
			CrudRepository<E, String> delegateRepository) {
		super(aggregateType, parameterResolverFactory);
		this.eventBus = eventBus;
		this.delegateRepository = delegateRepository;
	}

	@Override
	protected AnnotatedAggregate<A> doCreateNewForLock(Callable<A> factoryMethod) throws Exception {
		return AnnotatedAggregate.initialize(
				factoryMethod, aggregateModel(), eventBus, eventBus instanceof EventStore);
	}

	@Override
	protected void doSaveWithLock(AnnotatedAggregate<A> aggregate) {
		delegateRepository.save(convertToEntity(aggregate.getAggregateRoot()));
	}

	@Override
	protected void doDeleteWithLock(AnnotatedAggregate<A> aggregate) {
		delegateRepository.delete(convertToEntity(aggregate.getAggregateRoot()));
	}

	@Override
	protected AnnotatedAggregate<A> doLoadWithLock(String aggregateIdentifier, Long expectedVersion) {
		A aggregateRoot = delegateRepository.findById(aggregateIdentifier)
				.map(this::convertToAggregate)
				.orElseThrow(() -> new AggregateNotFoundException(aggregateIdentifier,
						format("Aggregate '%s' with identifier '%s' not found",
								getAggregateType().getSimpleName(), aggregateIdentifier)));

		AnnotatedAggregate<A> aggregate = AnnotatedAggregate.initialize(aggregateRoot, aggregateModel(), eventBus);

		if (eventBus instanceof EventStore) {
			Optional<Long> sequenceNumber = ((EventStore) eventBus).lastSequenceNumberFor(aggregateIdentifier);
			sequenceNumber.ifPresent(aggregate::initSequence);
		}
		return aggregate;
	}

	/**
	 * Converts the given aggregate instance to a Spring Data entity which can be used with the underlying Spring Data
	 * repository. The delegate repository calls this method when saving or deleting an aggregate.
	 *
	 * @param aggregate Aggregate instance to be converted to an entity.
	 * @return Entity instance representing the state of the given aggregate instance.
	 */
	protected abstract E convertToEntity(A aggregate);

	/**
	 * Converts the given entity instance to an aggregate which can be handled by the Axon Framework. The delegate
	 * repository calls this method when loading an aggregate by its identifier.
	 *
	 * @param entity Entity instance to be converted to an aggregate.
	 * @return Aggregate instance representing the state of the given entity instance.
	 */
	protected abstract A convertToAggregate(E entity);
}
