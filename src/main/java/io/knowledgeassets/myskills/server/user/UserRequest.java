package io.knowledgeassets.myskills.server.user;

import lombok.Data;

@Data
public class UserRequest {
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
}
