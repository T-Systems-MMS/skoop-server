package io.knowledgeassets.myskills.server.userskill.command;

import io.knowledgeassets.myskills.server.skill.command.SkillDeletedEvent;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryRepository;
import io.knowledgeassets.myskills.server.user.query.UserQueryRepository;
import io.knowledgeassets.myskills.server.userskill.query.UserSkillQueryRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.io.Serializable;
import java.util.Objects;

import static java.lang.String.format;
import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.axonframework.commandhandling.model.AggregateLifecycle.markDeleted;

@Aggregate
public class UserSkillAggregate {
	@AggregateIdentifier
	private UserSkillAggregateKey id;
	private Integer currentLevel;
	private Integer desiredLevel;
	private Integer priority;

	public UserSkillAggregate() {
	}

	@CommandHandler
	public UserSkillAggregate(CreateUserSkillCommand command, UserQueryRepository userQueryRepository,
							  SkillQueryRepository skillQueryRepository,
							  UserSkillQueryRepository userSkillQueryRepository) {
		// Validate if user and skill exist
		userQueryRepository.findById(command.getUserId())
				.orElseThrow(() -> new IllegalArgumentException(
						format("User with ID '%s' not found", command.getUserId())));

		skillQueryRepository.findById(command.getSkillId())
				.orElseThrow(() -> new IllegalArgumentException(
						format("Skill with ID '%s' not found", command.getSkillId())));

		// Validate if skill is not assigned to user yet
		if (userSkillQueryRepository.findByUserIdAndSkillId(command.getUserId(), command.getSkillId()).isPresent()) {
			throw new IllegalArgumentException(format("Skill with ID '%s' already assigned to user with ID '%s'",
					command.getSkillId(), command.getUserId()));
		}

		apply(new UserSkillCreatedEvent(new UserSkillAggregateKey(command.getUserId(), command.getSkillId()),
				command.getCurrentLevel(), command.getDesiredLevel(), command.getPriority()));
	}

	@EventSourcingHandler
	public void handle(UserSkillCreatedEvent event) {
		id = event.getId();
		currentLevel = event.getCurrentLevel();
		desiredLevel = event.getDesiredLevel();
		priority = event.getPriority();
	}

	@CommandHandler
	public void handle(UpdateUserSkillCommand command) {
		apply(new UserSkillUpdatedEvent(command.getId(), command.getCurrentLevel(), command.getDesiredLevel(),
				command.getPriority()));
	}

	@EventSourcingHandler
	public void handle(UserSkillUpdatedEvent event) {
		currentLevel = event.getCurrentLevel();
		desiredLevel = event.getDesiredLevel();
		priority = event.getPriority();
	}

	@CommandHandler
	public void handle(DeleteUserSkillCommand command) {
		apply(new UserSkillDeletedEvent(command.getId()));
	}

	@EventSourcingHandler
	public void handle(UserSkillDeletedEvent event) {
		markDeleted();
	}

	@EventHandler
	public void handle(SkillDeletedEvent event, UserSkillQueryRepository userSkillQueryRepository,
					   CommandGateway commandGateway) {
		userSkillQueryRepository.findBySkillId(event.getId()).forEach(userSkill -> {
			commandGateway.sendAndWait(new DeleteUserSkillCommand(UserSkillAggregateKey.fromString(userSkill.getId())));
		});
	}

	public UserSkillAggregateKey getId() {
		return id;
	}

	public void setId(UserSkillAggregateKey id) {
		this.id = id;
	}

	public UserSkillAggregate id(UserSkillAggregateKey id) {
		this.id = id;
		return this;
	}

	public Integer getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
	}

	public UserSkillAggregate currentLevel(Integer currentLevel) {
		this.currentLevel = currentLevel;
		return this;
	}

	public Integer getDesiredLevel() {
		return desiredLevel;
	}

	public void setDesiredLevel(Integer desiredLevel) {
		this.desiredLevel = desiredLevel;
	}

	public UserSkillAggregate desiredLevel(Integer desiredLevel) {
		this.desiredLevel = desiredLevel;
		return this;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public UserSkillAggregate priority(Integer priority) {
		this.priority = priority;
		return this;
	}

	public static class UserSkillAggregateKey implements Serializable {
		private final String userId;
		private final String skillId;

		public UserSkillAggregateKey(String userId, String skillId) {
			this.userId = userId;
			this.skillId = skillId;
		}

		public String getUserId() {
			return userId;
		}

		public String getSkillId() {
			return skillId;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			UserSkillAggregateKey that = (UserSkillAggregateKey) o;
			return Objects.equals(userId, that.userId) &&
					Objects.equals(skillId, that.skillId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(userId, skillId);
		}

		@Override
		public String toString() {
			return userId + ';' + skillId;
		}

		public static UserSkillAggregateKey fromString(String key) {
			String[] ids = key.split(";");
			return new UserSkillAggregateKey(ids[0], ids[1]);
		}
	}
}
