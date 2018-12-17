package io.knowledgeassets.myskills.server.userskill.command;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static io.knowledgeassets.myskills.server.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserSkillCommandController.class)
class UserSkillCommandControllerTests extends AbstractControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserSkillCommandService userSkillCommandService;

	@Test
	@DisplayName("Creates new relationship between given user and skill using skill ID")
	void createsAndReturnsNewRelationshipForGivenUserIdAndSkillId() throws Exception {
		User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(userSkillCommandService.createUserSkillBySkillId(
				"1f37fb2a-b4d0-4119-9113-4677beb20ae2", "cbf3a2f7-b5b8-46a8-85bf-aaa75a142a11",
				2, 3, 4))
				.willReturn(UserSkill.builder()
						.user(owner)
						.skill(Skill.builder()
								.id("cbf3a2f7-b5b8-46a8-85bf-aaa75a142a11")
								.name("Angular")
								.description("JavaScript Framework")
								.build())
						.currentLevel(2)
						.desiredLevel(3)
						.priority(4)
						.build());

		String requestContent = "{" +
				"\"skillId\":\"cbf3a2f7-b5b8-46a8-85bf-aaa75a142a11\"," +
				"\"currentLevel\":2," +
				"\"desiredLevel\":3," +
				"\"priority\":4" +
				"}";

		mockMvc.perform(post("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/skills")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestContent)
				.with(authentication(withUser(owner)))
				.with(csrf()))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.skill.id", is(equalTo("cbf3a2f7-b5b8-46a8-85bf-aaa75a142a11"))))
				.andExpect(jsonPath("$.skill.name", is(equalTo("Angular"))))
				.andExpect(jsonPath("$.skill.description", is(equalTo("JavaScript Framework"))))
				.andExpect(jsonPath("$.currentLevel", is(equalTo(2))))
				.andExpect(jsonPath("$.desiredLevel", is(equalTo(3))))
				.andExpect(jsonPath("$.priority", is(equalTo(4))));
	}

	@Test
	@DisplayName("Creates new relationship between given user and skill using skill name")
	void createsAndReturnsNewRelationshipForGivenUserIdAndSkillName() throws Exception {
		User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(userSkillCommandService.createUserSkillBySkillName(
				"1f37fb2a-b4d0-4119-9113-4677beb20ae2", "Angular", 2, 3, 4))
				.willReturn(UserSkill.builder()
						.user(owner)
						.skill(Skill.builder()
								.id("cbf3a2f7-b5b8-46a8-85bf-aaa75a142a11")
								.name("Angular")
								.description("JavaScript Framework")
								.build())
						.currentLevel(2)
						.desiredLevel(3)
						.priority(4)
						.build());

		String requestContent = "{" +
				"\"skillName\":\"Angular\"," +
				"\"currentLevel\":2," +
				"\"desiredLevel\":3," +
				"\"priority\":4" +
				"}";

		mockMvc.perform(post("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/skills")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestContent)
				.with(authentication(withUser(owner)))
				.with(csrf()))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.skill.id", is(equalTo("cbf3a2f7-b5b8-46a8-85bf-aaa75a142a11"))))
				.andExpect(jsonPath("$.skill.name", is(equalTo("Angular"))))
				.andExpect(jsonPath("$.skill.description", is(equalTo("JavaScript Framework"))))
				.andExpect(jsonPath("$.currentLevel", is(equalTo(2))))
				.andExpect(jsonPath("$.desiredLevel", is(equalTo(3))))
				.andExpect(jsonPath("$.priority", is(equalTo(4))));
	}

	@Test
	@DisplayName("Responds with status 400 if both the skill ID and skill name are missing in request")
	void yieldsBadRequestOnMissingSkillIdAndSkillName() throws Exception {
		User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		String requestContent = "{" +
				"\"currentLevel\":2," +
				"\"desiredLevel\":3," +
				"\"priority\":4" +
				"}";

		mockMvc.perform(post("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/skills")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestContent)
				.with(authentication(withUser(owner)))
				.with(csrf()))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andDo(result -> {
					then(userSkillCommandService).shouldHaveZeroInteractions();
				});
	}

	@Test
	@DisplayName("Responds with status 403 if foreign user attempts to create new relationship")
	void yieldsForbiddenOnForeignUserAuthentication() throws Exception {
		User foreigner = User.builder()
				.id("00969526-a93c-4190-975c-bb05a379ee63")
				.userName("testing")
				.build();

		String requestContent = "{" +
				"\"skillId\":\"cbf3a2f7-b5b8-46a8-85bf-aaa75a142a11\"," +
				"\"currentLevel\":2," +
				"\"desiredLevel\":3," +
				"\"priority\":4" +
				"}";

		mockMvc.perform(post("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/skills")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestContent)
				.with(authentication(withUser(foreigner)))
				.with(csrf()))
				.andExpect(status().isForbidden())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andDo(result -> {
					then(userSkillCommandService).shouldHaveZeroInteractions();
				});
	}
}
