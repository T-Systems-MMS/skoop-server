package io.knowledgeassets.myskills.server.user;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.user.query.UserQueryController;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserQueryController.class)
class UserQueryControllerTests extends AbstractControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserQueryService userQueryService;

	@Test
	@DisplayName("Responds with the list of users")
	void respondsWithListOfUsers() throws Exception {
		given(userQueryService.getUsers()).willReturn(Stream.of(
				User.builder()
						.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
						.userName("tester")
						.firstName("Toni")
						.lastName("Tester")
						.email("toni.tester@myskills.io")
						.coach(true)
						.build(),
				User.builder()
						.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
						.userName("testing")
						.firstName("Tina")
						.lastName("Testing")
						.email("tina.testing@myskills.io")
						.coach(false)
						.build()
		));

		mockMvc.perform(get("/users")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").roles("USER")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].id", is(equalTo("56ef4778-a084-4509-9a3e-80b7895cf7b0"))))
				.andExpect(jsonPath("$[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].firstName", is(equalTo("Toni"))))
				.andExpect(jsonPath("$[0].lastName", is(equalTo("Tester"))))
				.andExpect(jsonPath("$[0].email", is(equalTo("toni.tester@myskills.io"))))
				.andExpect(jsonPath("$[0].coach", is(equalTo(true))))
				.andExpect(jsonPath("$[1].id", is(equalTo("d9d74c04-0ab0-479c-a1d7-d372990f11b6"))))
				.andExpect(jsonPath("$[1].userName", is(equalTo("testing"))))
				.andExpect(jsonPath("$[1].firstName", is(equalTo("Tina"))))
				.andExpect(jsonPath("$[1].lastName", is(equalTo("Testing"))))
				.andExpect(jsonPath("$[1].email", is(equalTo("tina.testing@myskills.io"))))
				.andExpect(jsonPath("$[1].coach", is(equalTo(false))));
	}

	@Test
	@DisplayName("Responds with the requested user")
	void respondsWithRequestedUser() throws Exception {
		given(userQueryService.getUserById("56ef4778-a084-4509-9a3e-80b7895cf7b0"))
				.willReturn(Optional.of(User.builder()
						.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
						.userName("tester")
						.firstName("Toni")
						.lastName("Tester")
						.email("toni.tester@myskills.io")
						.coach(true)
						.build())
				);

		mockMvc.perform(get("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").roles("USER")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("56ef4778-a084-4509-9a3e-80b7895cf7b0"))))
				.andExpect(jsonPath("$.userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$.firstName", is(equalTo("Toni"))))
				.andExpect(jsonPath("$.lastName", is(equalTo("Tester"))))
				.andExpect(jsonPath("$.email", is(equalTo("toni.tester@myskills.io"))))
				.andExpect(jsonPath("$.coach", is(equalTo(true))));
	}
}
