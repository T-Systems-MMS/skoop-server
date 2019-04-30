package com.tsmms.skoop.userskill.query;

import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserPermissionScope;
import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.userskill.UserSkill;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.stream.Stream;

import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserSkillQueryController.class)
class UserSkillQueryControllerTests extends AbstractControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserSkillQueryService userSkillQueryService;

	@Test
	@DisplayName("Responds with the list of skill relationships for the given user")
	void providesSkillRelationshipsForGivenUserId() throws Exception {
		User owner = User.builder()
				.id("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("tester")
				.build();

		given(userSkillQueryService.getUserSkillsByUserId("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")).willReturn(Stream.of(
				UserSkill.builder()
						.id(1L)
						.user(owner)
						.skill(Skill.builder()
								.id("e441613b-319f-4698-917d-6a4037c8e330")
								.name("Angular")
								.description("JavaScript Framework")
								.build())
						.currentLevel(2)
						.desiredLevel(3)
						.priority(4)
						.build(),
				UserSkill.builder()
						.id(2L)
						.user(owner)
						.skill(Skill.builder()
								.id("3d4236c9-d84a-420a-baee-27b263118a28")
								.name("Spring Boot")
								.description("Java Framework")
								.build())
						.currentLevel(1)
						.desiredLevel(2)
						.priority(3)
						.build()
		));

		mockMvc.perform(get("/users/bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6/skills")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].skill.id", is(equalTo("e441613b-319f-4698-917d-6a4037c8e330"))))
				.andExpect(jsonPath("$[0].skill.name", is(equalTo("Angular"))))
				.andExpect(jsonPath("$[0].skill.description", is(equalTo("JavaScript Framework"))))
				.andExpect(jsonPath("$[0].currentLevel", is(equalTo(2))))
				.andExpect(jsonPath("$[0].desiredLevel", is(equalTo(3))))
				.andExpect(jsonPath("$[0].priority", is(equalTo(4))))
				.andExpect(jsonPath("$[1].skill.id", is(equalTo("3d4236c9-d84a-420a-baee-27b263118a28"))))
				.andExpect(jsonPath("$[1].skill.name", is(equalTo("Spring Boot"))))
				.andExpect(jsonPath("$[1].skill.description", is(equalTo("Java Framework"))))
				.andExpect(jsonPath("$[1].currentLevel", is(equalTo(1))))
				.andExpect(jsonPath("$[1].desiredLevel", is(equalTo(2))))
				.andExpect(jsonPath("$[1].priority", is(equalTo(3))));
	}

	@Test
	@DisplayName("Responds with the existing relationship between the given user and skill")
	void providesRelationshipForGivenUserIdAndSkillId() throws Exception {
		User owner = User.builder()
				.id("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("tester")
				.build();

		given(userSkillQueryService.getUserSkillByUserIdAndSkillId(
				"bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6", "e441613b-319f-4698-917d-6a4037c8e330"))
				.willReturn(Optional.of(UserSkill.builder()
						.id(1L)
						.user(owner)
						.skill(Skill.builder()
								.id("e441613b-319f-4698-917d-6a4037c8e330")
								.name("Angular")
								.description("JavaScript Framework")
								.build())
						.currentLevel(2)
						.desiredLevel(3)
						.priority(4)
						.build()
				));

		mockMvc.perform(get("/users/bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6/skills/e441613b-319f-4698-917d-6a4037c8e330")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.skill.id", is(equalTo("e441613b-319f-4698-917d-6a4037c8e330"))))
				.andExpect(jsonPath("$.skill.name", is(equalTo("Angular"))))
				.andExpect(jsonPath("$.skill.description", is(equalTo("JavaScript Framework"))))
				.andExpect(jsonPath("$.currentLevel", is(equalTo(2))))
				.andExpect(jsonPath("$.desiredLevel", is(equalTo(3))))
				.andExpect(jsonPath("$.priority", is(equalTo(4))));
	}

	@Test
	@DisplayName("Responds with status 404 if requested relationship between user and skill does not exist")
	void yieldsNotFoundForNonExistingRelationshipForGivenUserIdAndSkillId() throws Exception {
		User owner = User.builder()
				.id("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("tester")
				.build();

		given(userSkillQueryService.getUserSkillByUserIdAndSkillId(
				"bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6", "e441613b-319f-4698-917d-6a4037c8e330"))
				.willReturn(Optional.empty());

		mockMvc.perform(get("/users/bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6/skills/e441613b-319f-4698-917d-6a4037c8e330")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	@DisplayName("Responds with the list of coaches for the given user and skill")
	void providesCoachesForGivenUserIdAndSkillId() throws Exception {
		User owner = User.builder()
				.id("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("tester")
				.build();

		given(userSkillQueryService.getCoachesByUserIdAndSkillId(
				"bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6", "e441613b-319f-4698-917d-6a4037c8e330"))
				.willReturn(Stream.of(
						User.builder()
								.id("c749d708-6ef4-4ce8-9a86-220a70065326")
								.userName("testing")
								.firstName("Tina")
								.lastName("Testing")
								.email("tina.testing@skoop.io")
								.build(),
						User.builder()
								.id("e8a4f522-e662-4ead-b3ce-b004f3bdcde5")
								.userName("testbed")
								.firstName("Tabia")
								.lastName("Testbed")
								.email("tabia.testbed@skoop.io")
								.build()
				));

		mockMvc.perform(get("/users/bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6/skills/e441613b-319f-4698-917d-6a4037c8e330/coaches")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].id", is(equalTo("c749d708-6ef4-4ce8-9a86-220a70065326"))))
				.andExpect(jsonPath("$[0].userName", is(equalTo("testing"))))
				.andExpect(jsonPath("$[0].firstName", is(equalTo("Tina"))))
				.andExpect(jsonPath("$[0].lastName", is(equalTo("Testing"))))
				.andExpect(jsonPath("$[0].email", is(equalTo("tina.testing@skoop.io"))))
				.andExpect(jsonPath("$[0].academicDegree").doesNotExist())
				.andExpect(jsonPath("$[0].positionProfile").doesNotExist())
				.andExpect(jsonPath("$[0].summary").doesNotExist())
				.andExpect(jsonPath("$[0].industrySectors").doesNotExist())
				.andExpect(jsonPath("$[0].specializations").doesNotExist())
				.andExpect(jsonPath("$[0].certificates").doesNotExist())
				.andExpect(jsonPath("$[0].languages").doesNotExist())
				.andExpect(jsonPath("$[1].id", is(equalTo("e8a4f522-e662-4ead-b3ce-b004f3bdcde5"))))
				.andExpect(jsonPath("$[1].userName", is(equalTo("testbed"))))
				.andExpect(jsonPath("$[1].firstName", is(equalTo("Tabia"))))
				.andExpect(jsonPath("$[1].lastName", is(equalTo("Testbed"))))
				.andExpect(jsonPath("$[1].email", is(equalTo("tabia.testbed@skoop.io"))))
				.andExpect(jsonPath("$[1].academicDegree").doesNotExist())
				.andExpect(jsonPath("$[1].positionProfile").doesNotExist())
				.andExpect(jsonPath("$[1].summary").doesNotExist())
				.andExpect(jsonPath("$[1].industrySectors").doesNotExist())
				.andExpect(jsonPath("$[1].specializations").doesNotExist())
				.andExpect(jsonPath("$[1].certificates").doesNotExist())
				.andExpect(jsonPath("$[1].languages").doesNotExist());
	}

	@Test
	@DisplayName("Responds with the list of skill relationships for the given user and authorized principal")
	void providesSkillRelationshipsToAuthorizedForeignUser() throws Exception {
		User owner = User.builder()
				.id("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("tester")
				.build();
		User foreigner = User.builder()
				.id("c749d708-6ef4-4ce8-9a86-220a70065326")
				.userName("testing")
				.build();

		given(userSkillQueryService.getUserSkillsByUserId("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")).willReturn(Stream.of(
				UserSkill.builder()
						.id(1L)
						.user(owner)
						.skill(Skill.builder()
								.id("e441613b-319f-4698-917d-6a4037c8e330")
								.name("Angular")
								.description("JavaScript Framework")
								.build())
						.currentLevel(2)
						.desiredLevel(3)
						.priority(4)
						.build()
		));

		givenUser(owner.getId())
				.hasAuthorizedUsers(foreigner.getId())
				.forScopes(UserPermissionScope.READ_USER_SKILLS);

		mockMvc.perform(get("/users/bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6/skills")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(foreigner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(1))))
				.andExpect(jsonPath("$[0].skill.id", is(equalTo("e441613b-319f-4698-917d-6a4037c8e330"))))
				.andExpect(jsonPath("$[0].skill.name", is(equalTo("Angular"))))
				.andExpect(jsonPath("$[0].skill.description", is(equalTo("JavaScript Framework"))))
				.andExpect(jsonPath("$[0].currentLevel", is(equalTo(2))))
				.andExpect(jsonPath("$[0].desiredLevel", is(equalTo(3))))
				.andExpect(jsonPath("$[0].priority", is(equalTo(4))));
	}

	@Test
	@DisplayName("Responds with status 403 if principal is not authorized to read the skill relationships")
	void yieldsForbiddenOnNonAuthorizedAccessToSkillRelationships() throws Exception {
		User foreigner = User.builder()
				.id("c749d708-6ef4-4ce8-9a86-220a70065326")
				.userName("testing")
				.build();

		mockMvc.perform(get("/users/bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6/skills")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(foreigner))))
				.andExpect(status().isForbidden())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andDo(result -> {
					then(userSkillQueryService).shouldHaveZeroInteractions();
				});
	}
}
