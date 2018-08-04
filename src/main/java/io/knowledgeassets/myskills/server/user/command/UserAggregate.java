package io.knowledgeassets.myskills.server.user.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.common.IdentifierFactory;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.axonframework.commandhandling.model.AggregateLifecycle.markDeleted;

@Aggregate
public class UserAggregate {
	@AggregateIdentifier
	private String id;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;

	public UserAggregate() {
	}

	@CommandHandler
	public UserAggregate(CreateUserCommand command) {
		apply(new UserCreatedEvent(IdentifierFactory.getInstance().generateIdentifier(), command.getUserName(),
				command.getFirstName(), command.getLastName(), command.getEmail()));
	}

	@EventSourcingHandler
	public void handle(UserCreatedEvent event) {
		id = event.getId();
		userName = event.getUserName();
		firstName = event.getFirstName();
		lastName = event.getLastName();
		email = event.getEmail();
	}

	@CommandHandler
	public void handle(UpdateUserCommand command) {
		apply(new UserUpdatedEvent(command.getId(), command.getUserName(), command.getFirstName(),
				command.getLastName(), command.getEmail()));
	}

	@EventSourcingHandler
	public void handle(UserUpdatedEvent event) {
		userName = event.getUserName();
		firstName = event.getFirstName();
		lastName = event.getLastName();
		email = event.getEmail();
	}

	@CommandHandler
	public void handle(DeleteUserCommand command) {
		apply(new UserDeletedEvent(command.getId()));
	}

	@EventSourcingHandler
	public void handle(UserDeletedEvent event) {
		markDeleted();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserAggregate id(String id) {
		this.id = id;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserAggregate userName(String userName) {
		this.userName = userName;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public UserAggregate firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UserAggregate lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserAggregate email(String email) {
		this.email = email;
		return this;
	}
}
