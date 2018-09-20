package io.knowledgeassets.myskills.server.user;

import io.knowledgeassets.myskills.server.common.Neo4jSessionFactoryConfiguration;
import io.knowledgeassets.myskills.server.user.query.UserQueryController;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserQueryController.class)
// Additional configuration is required to workaround missing SessionFactory issue!
@Import(Neo4jSessionFactoryConfiguration.class)
class UserQueryControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserQueryService userQueryService;

	@Test
	@DisplayName("Responds with the list of users provided by the internal service")
	void respondsWithListOfUsers() throws Exception {
		given(userQueryService.getUsers()).willReturn(Stream.of(
				User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").build(),
				User.builder().id("456").userName("tester2").firstName("secondTester").email("tester2@gmail.com").build()
		));
		mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON)
				.with(user("tester").password("123").roles("USER")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].id", is(equalTo("123"))))
				.andExpect(jsonPath("$[0].userName", is(equalTo("tester1"))))
				.andExpect(jsonPath("$[0].email", is(equalTo("tester1@gmail.com"))))
				.andExpect(jsonPath("$[1].id", is(equalTo("456"))))
				.andExpect(jsonPath("$[1].userName", is(equalTo("tester2"))))
				.andExpect(jsonPath("$[1].lastName", is(equalTo(null))));
	}

	@Test
	@DisplayName("Responds with the single user provided by the internal service")
	void respondsWithRequestedUser() throws Exception {
		given(userQueryService.getUserById("123")).willReturn(Optional.of(
				User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").build()));
		mockMvc.perform(get("/users/123").accept(MediaType.APPLICATION_JSON)
				.with(user("tester").password("123").roles("USER")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.userName", is(equalTo("tester1"))))
				.andExpect(jsonPath("$.email", is(equalTo("tester1@gmail.com"))));
	}
}
