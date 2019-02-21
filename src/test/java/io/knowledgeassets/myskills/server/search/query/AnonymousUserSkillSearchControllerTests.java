package io.knowledgeassets.myskills.server.search.query;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.search.AnonymousUserSkillResult;
import io.knowledgeassets.myskills.server.search.UserSearchSkillCriterion;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.stream.Stream;

import static io.knowledgeassets.myskills.server.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnonymousUserSkillSearchController.class)
class AnonymousUserSkillSearchControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AnonymousUserSkillSearchService anonymousUserSkillSearchService;

	@Test
	@DisplayName("Tests if BAD_REQUEST status code is returned when wrong search parameters are passed.")
	void testIfBadRequestStatusCodeIsReturnedWhenWrongSearchParametersArePassed() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		mockMvc.perform(get("/search/anonymous-user-profiles")
				.param("params", "A2", "B1")
				.with(authentication(withUser(owner))))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Tests if BAD_REQUEST status code is returned when getting anonymous user skills with no parameters to filter by.")
	void testIfBadRequestStatusCodeIsReturnedWhenThereAreNoParameterToFilterUserSkillsBy() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		mockMvc.perform(get("/search/anonymous-user-profiles")
				.with(authentication(withUser(owner))))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Tests if BAD_REQUEST status code is returned when getting anonymous user skills with empty parameter list to filter by.")
	void testIfBadRequestStatusCodeIsReturnedWhenThereAreEmptyParameterListToFilterUserSkillsBy() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		mockMvc.perform(get("/search/anonymous-user-profiles")
				.param("params", "")
				.with(authentication(withUser(owner))))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Tests if UNAUTHORIZED status code is returned when a user sending a request is unknown.")
	void testIfUnauthorizedStatusCodeIsReturnedWhenUserIsUnknown() throws Exception {
		mockMvc.perform(get("/search/anonymous-user-profiles"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("Test if anonymous user skills are successfully retrieved.")
	void testIfAnonymousUserSkillsAreSuccessfullyRetrieved() throws Exception {

		given(anonymousUserSkillSearchService.findBySkillLevel(Arrays.asList(UserSearchSkillCriterion.builder()
						.skillId("B")
						.minimumCurrentLevel(2)
						.build(),
				UserSearchSkillCriterion.builder()
						.skillId("A")
						.minimumCurrentLevel(1)
						.build()
				))
		).willReturn(Stream.of(AnonymousUserSkillResult.builder()
						.referenceId("ref1")
						.userSkills(Arrays.asList(
								UserSkill.builder()
										.currentLevel(1)
										.skill(Skill.builder()
												.name("Angular")
												.build())
										.build(),
								UserSkill.builder()
										.currentLevel(2)
										.skill(Skill.builder()
												.name("Spring Boot")
												.build())
										.build()
								)
						)
						.build()
				)
		);

		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		mockMvc.perform(get("/search/anonymous-user-profiles")
				.param("params", "B+2", "A+1")
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(equalTo(1))))
				.andExpect(jsonPath("$[0].userReferenceId", is(equalTo("ref1"))))
				.andExpect(jsonPath("$[0].skills.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].skills[0].skillName", is(equalTo("Angular"))))
				.andExpect(jsonPath("$[0].skills[0].currentLevel", is(equalTo(1))))
				.andExpect(jsonPath("$[0].skills[1].skillName", is(equalTo("Spring Boot"))))
				.andExpect(jsonPath("$[0].skills[1].currentLevel", is(equalTo(2))));

	}


}
