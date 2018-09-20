package io.knowledgeassets.myskills.server.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

	@NotBlank
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private Boolean coach;
}
