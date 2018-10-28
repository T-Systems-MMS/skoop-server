package io.knowledgeassets.myskills.server.userskill.command;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserSkillCommandController.class)
// Additional configuration is required to workaround missing SessionFactory issue!
// TODO: Add class SecurityConfiguration to enable real security in the test.
@Import(Neo4jSessionFactoryConfiguration.class)
class UserSkillCommandControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserSkillCommandService userSkillCommandService;

	@Test
	@WithMockUser("tester")
	@DisplayName("Creates new relationship between given user and skill using skill ID")
	void createsAndReturnsNewRelationshipForGivenUserIdAndSkillId() throws Exception {
		given(userSkillCommandService.createUserSkillBySkillId("123", "ABC", 2, 3, 4))
				.willReturn(UserSkill.builder()
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
						.build());
		mockMvc.perform(post("/users/123/skills")
				.accept(MediaType.APPLICATION_JSON)
				.with(csrf())
				// TODO: Generate valid JWT and add it as bearer token to "Authorization" header. Remove mock user.
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"skillId\":\"ABC\",\"currentLevel\":2,\"desiredLevel\":3,\"priority\":4}"))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.skill.id", is(equalTo("ABC"))))
				.andExpect(jsonPath("$.skill.name", is(equalTo("Angular"))))
				.andExpect(jsonPath("$.skill.description", is(equalTo("JavaScript Framework"))))
				.andExpect(jsonPath("$.currentLevel", is(equalTo(2))))
				.andExpect(jsonPath("$.desiredLevel", is(equalTo(3))))
				.andExpect(jsonPath("$.priority", is(equalTo(4))));
	}

	@Test
	@WithMockUser("tester")
	@DisplayName("Creates new relationship between given user and skill using skill name")
	void createsAndReturnsNewRelationshipForGivenUserIdAndSkillName() throws Exception {
		given(userSkillCommandService.createUserSkillBySkillName("123", "Angular", 2, 3, 4))
				.willReturn(UserSkill.builder()
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
						.build());
		mockMvc.perform(post("/users/123/skills")
				.accept(MediaType.APPLICATION_JSON)
				.with(csrf())
				// TODO: Generate valid JWT and add it as bearer token to "Authorization" header. Remove mock user.
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"skillName\":\"Angular\",\"currentLevel\":2,\"desiredLevel\":3,\"priority\":4}"))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.skill.id", is(equalTo("ABC"))))
				.andExpect(jsonPath("$.skill.name", is(equalTo("Angular"))))
				.andExpect(jsonPath("$.skill.description", is(equalTo("JavaScript Framework"))))
				.andExpect(jsonPath("$.currentLevel", is(equalTo(2))))
				.andExpect(jsonPath("$.desiredLevel", is(equalTo(3))))
				.andExpect(jsonPath("$.priority", is(equalTo(4))));
	}

	@Test
	@WithMockUser("tester")
	@DisplayName("Responds with status 400 if both the skill ID and skill name are not given")
	void yieldsBadRequestOnMissingSkillIdAndSkillName() throws Exception {
		mockMvc.perform(post("/users/123/skills")
				.accept(MediaType.APPLICATION_JSON)
				.with(csrf())
				// TODO: Generate valid JWT and add it as bearer token to "Authorization" header. Remove mock user.
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"currentLevel\":2,\"desiredLevel\":3,\"priority\":4}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
}