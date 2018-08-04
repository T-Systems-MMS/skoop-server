package io.knowledgeassets.myskills.server.user.command;

import io.knowledgeassets.myskills.server.user.query.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
public class UserCommandService {
	private CommandGateway commandGateway;
	private UserQueryRepository userQueryRepository;

	public UserCommandService(CommandGateway commandGateway, UserQueryRepository userQueryRepository) {
		this.commandGateway = commandGateway;
		this.userQueryRepository = userQueryRepository;
	}

	@Transactional
	public User createUser(String userName) {
		String id = commandGateway.sendAndWait(new CreateUserCommand(userName, null, null, null));
		return userQueryRepository.findById(id).orElseThrow(() -> new IllegalStateException(
				format("User with ID '%s' not found", id)));
	}

	@Transactional
	public User createUser(String userName, String firstName, String lastName, String email) {
		String id = commandGateway.sendAndWait(new CreateUserCommand(userName, firstName, lastName, email));
		return userQueryRepository.findById(id).orElseThrow(() -> new IllegalStateException(
				format("User with ID '%s' not found", id)));
	}

	@Transactional
	public User updateUser(String id, String userName, String firstName, String lastName, String email) {
		commandGateway.sendAndWait(new UpdateUserCommand(id, userName, firstName, lastName, email));
		return userQueryRepository.findById(id).orElseThrow(() -> new IllegalStateException(
				format("User with ID '%s' not found", id)));
	}

	@Transactional
	public void deleteUser(String id) {
		commandGateway.sendAndWait(new DeleteUserCommand(id));
	}
}
