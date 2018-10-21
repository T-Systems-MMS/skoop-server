package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.common.Neo4jSessionFactoryConfiguration;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
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
@WebMvcTest(UserSkillQueryController.class)
// Additional configuration is required to workaround missing SessionFactory issue!
@Import(Neo4jSessionFactoryConfiguration.class)
class UserSkillQueryControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserSkillQueryService userSkillQueryService;

	@Test
	@DisplayName("Responds with the list of skills related to the given user")
	void providesSkillRelationshipsForGivenUserId() throws Exception {
		User user = User.builder()
				.id("123")
				.userName("tester")
				.build();
		given(userSkillQueryService.getUserSkillsByUserId("123")).willReturn(Stream.of(
				UserSkill.builder()
						.id("123;ABC")
						.user(user)
						.skill(Skill.builder()
								.id("ABC")
								.name("Angular")
								.description("JavaScript Framework")
								.build())
						.currentLevel(2)
						.desiredLevel(3)
						.priority(4)
						.build(),
				UserSkill.builder()
						.id("123;DEF")
						.user(user)
						.skill(Skill.builder()
								.id("DEF")
								.name("Spring Boot")
								.description("Java Framework")
								.build())
						.currentLevel(1)
						.desiredLevel(2)
						.priority(3)
						.build()
		));
		mockMvc.perform(get("/users/123/skills")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").password("secret").roles("USER")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].skill.id", is(equalTo("ABC"))))
				.andExpect(jsonPath("$[0].skill.name", is(equalTo("Angular"))))
				.andExpect(jsonPath("$[0].skill.description", is(equalTo("JavaScript Framework"))))
				.andExpect(jsonPath("$[0].currentLevel", is(equalTo(2))))
				.andExpect(jsonPath("$[0].desiredLevel", is(equalTo(3))))
				.andExpect(jsonPath("$[0].priority", is(equalTo(4))))
				.andExpect(jsonPath("$[1].skill.id", is(equalTo("DEF"))))
				.andExpect(jsonPath("$[1].skill.name", is(equalTo("Spring Boot"))))
				.andExpect(jsonPath("$[1].skill.description", is(equalTo("Java Framework"))))
				.andExpect(jsonPath("$[1].currentLevel", is(equalTo(1))))
				.andExpect(jsonPath("$[1].desiredLevel", is(equalTo(2))))
				.andExpect(jsonPath("$[1].priority", is(equalTo(3))));
	}

	@Test
	@DisplayName("Responds with the existing relationship between the given user and skill")
	void providesRelationshipForGivenUserIdAndSkillId() throws Exception {
		given(userSkillQueryService.getUserSkillByUserIdAndSkillId("123", "ABC")).willReturn(Optional.of(
				UserSkill.builder()
						.id("123;ABC")
						.user(User.builder()
								.id("123")
								.userName("tester")
								.build())
						.skill(Skill.builder()
								.id("ABC")
								.name("Angular")
								.description("JavaScript Framework")
								.build())
						.currentLevel(2)
						.desiredLevel(3)
						.priority(4)
						.build()
		));
		mockMvc.perform(get("/users/123/skills/ABC")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").password("secret").roles("USER")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.skill.id", is(equalTo("ABC"))))
				.andExpect(jsonPath("$.skill.name", is(equalTo("Angular"))))
				.andExpect(jsonPath("$.skill.description", is(equalTo("JavaScript Framework"))))
				.andExpect(jsonPath("$.currentLevel", is(equalTo(2))))
				.andExpect(jsonPath("$.desiredLevel", is(equalTo(3))))
				.andExpect(jsonPath("$.priority", is(equalTo(4))));
	}

	@Test
	@DisplayName("Responds with status 404 if requested relationship between user and skill does not exist")
	void yieldsNotFoundForNonExistingRelationshipForGivenUserIdAndSkillId() throws Exception {
		given(userSkillQueryService.getUserSkillByUserIdAndSkillId("123", "ABC")).willReturn(Optional.empty());
		mockMvc.perform(get("/users/123/skills/ABC")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").password("secret").roles("USER")))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	@DisplayName("Responds with the list of coaches for the given user and skill")
	void providesCoachesForGivenUserIdAndSkillId() throws Exception {
		given(userSkillQueryService.getCoachesByUserIdAndSkillId("123", "ABC")).willReturn(Stream.of(
				User.builder()
						.id("234")
						.userName("toni")
						.firstName("Toni")
						.lastName("Tester")
						.email("toni.tester@myskills.io")
						.coach(true)
						.build(),
				User.builder()
						.id("345")
						.userName("tina")
						.firstName("Tina")
						.lastName("Testing")
						.email("tina.testing@myskills.io")
						.coach(true)
						.build()
		));
		mockMvc.perform(get("/users/123/skills/ABC/coaches")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").password("secret").roles("USER")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].id", is(equalTo("234"))))
				.andExpect(jsonPath("$[0].userName", is(equalTo("toni"))))
				.andExpect(jsonPath("$[0].firstName", is(equalTo("Toni"))))
				.andExpect(jsonPath("$[0].lastName", is(equalTo("Tester"))))
				.andExpect(jsonPath("$[0].email", is(equalTo("toni.tester@myskills.io"))))
				.andExpect(jsonPath("$[0].coach", is(equalTo(true))))
				.andExpect(jsonPath("$[1].id", is(equalTo("345"))))
				.andExpect(jsonPath("$[1].userName", is(equalTo("tina"))))
				.andExpect(jsonPath("$[1].firstName", is(equalTo("Tina"))))
				.andExpect(jsonPath("$[1].lastName", is(equalTo("Testing"))))
				.andExpect(jsonPath("$[1].email", is(equalTo("tina.testing@myskills.io"))))
				.andExpect(jsonPath("$[1].coach", is(equalTo(true))));
	}
}
