package io.knowledgeassets.myskills.server.skill.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.common.IdentifierFactory;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.axonframework.commandhandling.model.AggregateLifecycle.markDeleted;

@Aggregate
public class SkillAggregate {
	@AggregateIdentifier
	private String id;
	private String name;
	private String description;

	public SkillAggregate() {
	}

	@CommandHandler
	public SkillAggregate(CreateSkillCommand command) {
		apply(new SkillCreatedEvent(IdentifierFactory.getInstance().generateIdentifier(), command.getName(),
				command.getDescription()));
	}

	@EventSourcingHandler
	public void handle(SkillCreatedEvent event) {
		id = event.getId();
		name = event.getName();
		description = event.getDescription();
	}

	@CommandHandler
	public void handle(UpdateSkillCommand command) {
		apply(new SkillUpdatedEvent(command.getId(), command.getName(), command.getDescription()));
	}

	@EventSourcingHandler
	public void handle(SkillUpdatedEvent event) {
		name = event.getName();
		description = event.getDescription();
	}

	@CommandHandler
	public void handle(DeleteSkillCommand command) {
		apply(new SkillDeletedEvent(command.getId()));
	}

	@EventSourcingHandler
	public void handle(SkillDeletedEvent event) {
		markDeleted();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SkillAggregate id(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SkillAggregate name(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SkillAggregate description(String description) {
		this.description = description;
		return this;
	}
}
