package io.knowledgeassets.myskills.server.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRequest {

	@NotBlank
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private Boolean coach;
}
