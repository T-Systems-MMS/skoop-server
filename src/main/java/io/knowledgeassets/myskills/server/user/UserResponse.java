package io.knowledgeassets.myskills.server.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
	private String id;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
}
