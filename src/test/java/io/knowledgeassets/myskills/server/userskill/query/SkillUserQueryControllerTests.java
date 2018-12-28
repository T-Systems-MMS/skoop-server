package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static io.knowledgeassets.myskills.server.common.JwtAuthenticationFactory.withUser;
import static io.knowledgeassets.myskills.server.user.UserPermissionScope.READ_USER_SKILLS;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SkillUserQueryController.class)
class SkillUserQueryControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserSkillQueryService userSkillQueryService;

	@Test
	@DisplayName("Responds with the list of users related to a specific skill. The list contains the only authenticated user.")
	void testGettingAuthorizedUserRelatedToSpecificSkill() throws Exception {
		final String skillId = "7bdcbcc7-1842-42f5-8370-5b6e31a23cfb";
		final Skill springBootSkill = Skill.builder()
				.id(skillId)
				.name("Spring Boot")
				.description("Sprint Boot description")
				.build();
		final User johnDoeUser = User.builder()
				.id("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("johndoe")
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@mail.com")
				.coach(false)
				.academicDegree("Diplom-Wirtschaftsinformatiker")
				.positionProfile("Software Developer")
				.summary("Developer")
				.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
				.specializations(Arrays.asList("IT Consulting", "Software Integration"))
				.certificates(Collections.singletonList("Java Certified Programmer"))
				.languages(Collections.singletonList("Deutsch"))
				.build();
		final User maryDoeUser = User.builder()
				.id("acdc938c-8e0d-4e00-98a8-da7b55aa5dd6")
				.userName("marydoe")
				.firstName("Mary")
				.lastName("Doe")
				.email("mary.doe@mail.com")
				.coach(false)
				.academicDegree("Diplom-Wirtschaftsinformatiker")
				.positionProfile("Software Architect")
				.summary("Architect")
				.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
				.specializations(Arrays.asList("IT Consulting", "Software Integration"))
				.certificates(Collections.singletonList("Scala Certified Programmer"))
				.languages(Collections.singletonList("English"))
				.build();

		given(userSkillQueryService.getBySkillId(skillId, null))
				.willReturn(Stream.of(
						UserSkill.builder()
							.id(1L)
							.skill(springBootSkill)
							.user(johnDoeUser)
							.currentLevel(1)
							.desiredLevel(2)
							.priority(2)
						.build(),
						UserSkill.builder()
							.id(2L)
							.skill(springBootSkill)
							.user(maryDoeUser)
							.currentLevel(2)
							.desiredLevel(3)
							.priority(1)
						.build()
				));

		User owner = User.builder()
				.id("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("tester")
				.build();

		mockMvc.perform(get("/skills/7bdcbcc7-1842-42f5-8370-5b6e31a23cfb/users")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(1))))
				.andExpect(jsonPath("$[0].currentLevel", is(equalTo(1))))
				.andExpect(jsonPath("$[0].desiredLevel", is(equalTo(2))))
				.andExpect(jsonPath("$[0].priority", is(equalTo(2))))
				.andExpect(jsonPath("$[0].user.id", is(equalTo("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6"))))
				.andExpect(jsonPath("$[0].user.userName", is(equalTo("johndoe"))))
				.andExpect(jsonPath("$[0].user.firstName", is(equalTo("John"))))
				.andExpect(jsonPath("$[0].user.lastName", is(equalTo("Doe"))))
				.andExpect(jsonPath("$[0].user.email", is(equalTo("john.doe@mail.com"))))
				.andExpect(jsonPath("$[0].user.coach", is(equalTo(false))))
				.andExpect(jsonPath("$[0].user.academicDegree").doesNotExist())
				.andExpect(jsonPath("$[0].user.positionProfile").doesNotExist())
				.andExpect(jsonPath("$[0].user.summary").doesNotExist())
				.andExpect(jsonPath("$[0].user.specializations").doesNotExist())
				.andExpect(jsonPath("$[0].user.certificates").doesNotExist())
				.andExpect(jsonPath("$[0].user.languages").doesNotExist());
	}

	@Test
	@DisplayName("Responds with the list of users related to a specific skill. The list contains the authenticated user and the user who let the authorized one to see her skills.")
	void testGettingAuthorizedUserAndAllowedUserRelatedToSpecificSkill() throws Exception {
		final String skillId = "7bdcbcc7-1842-42f5-8370-5b6e31a23cfb";
		final Skill springBootSkill = Skill.builder()
				.id(skillId)
				.name("Spring Boot")
				.description("Sprint Boot description")
				.build();
		final User johnDoeUser = User.builder()
				.id("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("johndoe")
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@mail.com")
				.coach(false)
				.academicDegree("Diplom-Wirtschaftsinformatiker")
				.positionProfile("Software Developer")
				.summary("Developer")
				.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
				.specializations(Arrays.asList("IT Consulting", "Software Integration"))
				.certificates(Collections.singletonList("Java Certified Programmer"))
				.languages(Collections.singletonList("Deutsch"))
				.build();
		final User maryDoeUser = User.builder()
				.id("acdc938c-8e0d-4e00-98a8-da7b55aa5dd6")
				.userName("marydoe")
				.firstName("Mary")
				.lastName("Doe")
				.email("mary.doe@mail.com")
				.coach(false)
				.academicDegree("Diplom-Wirtschaftsinformatiker")
				.positionProfile("Software Architect")
				.summary("Architect")
				.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
				.specializations(Arrays.asList("IT Consulting", "Software Integration"))
				.certificates(Collections.singletonList("Scala Certified Programmer"))
				.languages(Collections.singletonList("English"))
				.build();

		given(userPermissionQueryService.getUsersWhoGrantedPermission(
				"bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6", READ_USER_SKILLS))
				.willReturn(Stream.of(maryDoeUser));

		given(userSkillQueryService.getBySkillId(skillId, null))
				.willReturn(Stream.of(
						UserSkill.builder()
								.id(1L)
								.skill(springBootSkill)
								.user(johnDoeUser)
								.currentLevel(1)
								.desiredLevel(2)
								.priority(2)
								.build(),
						UserSkill.builder()
								.id(2L)
								.skill(springBootSkill)
								.user(maryDoeUser)
								.currentLevel(2)
								.desiredLevel(3)
								.priority(1)
								.build()
				));

		User owner = User.builder()
				.id("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("tester")
				.build();

		mockMvc.perform(get("/skills/7bdcbcc7-1842-42f5-8370-5b6e31a23cfb/users")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].currentLevel", is(equalTo(1))))
				.andExpect(jsonPath("$[0].desiredLevel", is(equalTo(2))))
				.andExpect(jsonPath("$[0].priority", is(equalTo(2))))
				.andExpect(jsonPath("$[0].user.id", is(equalTo("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6"))))
				.andExpect(jsonPath("$[0].user.userName", is(equalTo("johndoe"))))
				.andExpect(jsonPath("$[0].user.firstName", is(equalTo("John"))))
				.andExpect(jsonPath("$[0].user.lastName", is(equalTo("Doe"))))
				.andExpect(jsonPath("$[0].user.email", is(equalTo("john.doe@mail.com"))))
				.andExpect(jsonPath("$[0].user.coach", is(equalTo(false))))
				.andExpect(jsonPath("$[0].user.academicDegree").doesNotExist())
				.andExpect(jsonPath("$[0].user.positionProfile").doesNotExist())
				.andExpect(jsonPath("$[0].user.summary").doesNotExist())
				.andExpect(jsonPath("$[0].user.specializations").doesNotExist())
				.andExpect(jsonPath("$[0].user.certificates").doesNotExist())
				.andExpect(jsonPath("$[0].user.languages").doesNotExist())
				.andExpect(jsonPath("$[1].currentLevel", is(equalTo(2))))
				.andExpect(jsonPath("$[1].desiredLevel", is(equalTo(3))))
				.andExpect(jsonPath("$[1].priority", is(equalTo(1))))
				.andExpect(jsonPath("$[1].user.id", is(equalTo("acdc938c-8e0d-4e00-98a8-da7b55aa5dd6"))))
				.andExpect(jsonPath("$[1].user.userName", is(equalTo("marydoe"))))
				.andExpect(jsonPath("$[1].user.firstName", is(equalTo("Mary"))))
				.andExpect(jsonPath("$[1].user.lastName", is(equalTo("Doe"))))
				.andExpect(jsonPath("$[1].user.email", is(equalTo("mary.doe@mail.com"))))
				.andExpect(jsonPath("$[1].user.coach", is(equalTo(false))))
				.andExpect(jsonPath("$[1].user.academicDegree").doesNotExist())
				.andExpect(jsonPath("$[1].user.positionProfile").doesNotExist())
				.andExpect(jsonPath("$[1].user.summary").doesNotExist())
				.andExpect(jsonPath("$[1].user.specializations").doesNotExist())
				.andExpect(jsonPath("$[1].user.certificates").doesNotExist())
				.andExpect(jsonPath("$[1].user.languages").doesNotExist());
	}

	@Test
	@DisplayName("Gets authorized user related to a specific skill.")
	void testGettingAuthorizedUserRelatedToSkill() throws Exception {
		final String skillId = "7bdcbcc7-1842-42f5-8370-5b6e31a23cfb";
		final Skill springBootSkill = Skill.builder()
				.id(skillId)
				.name("Spring Boot")
				.description("Sprint Boot description")
				.build();
		final User johnDoeUser = User.builder()
				.id("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("johndoe")
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@mail.com")
				.coach(false)
				.academicDegree("Diplom-Wirtschaftsinformatiker")
				.positionProfile("Software Developer")
				.summary("Developer")
				.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
				.specializations(Arrays.asList("IT Consulting", "Software Integration"))
				.certificates(Collections.singletonList("Java Certified Programmer"))
				.languages(Collections.singletonList("Deutsch"))
				.build();

		given(userSkillQueryService.getUserSkillByUserIdAndSkillId("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6", skillId))
				.willReturn(Optional.of(UserSkill.builder()
						.id(1L)
						.skill(springBootSkill)
						.user(johnDoeUser)
						.currentLevel(1)
						.desiredLevel(2)
						.priority(2)
						.build()));

		final User owner = User.builder()
				.id("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")
				.userName("tester")
				.build();

		mockMvc.perform(get("/skills/7bdcbcc7-1842-42f5-8370-5b6e31a23cfb/users/bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.currentLevel", is(equalTo(1))))
				.andExpect(jsonPath("$.desiredLevel", is(equalTo(2))))
				.andExpect(jsonPath("$.priority", is(equalTo(2))))
				.andExpect(jsonPath("$.user.id", is(equalTo("bcbc938c-8e0d-4e00-98a8-da7b44aa5dd6"))))
				.andExpect(jsonPath("$.user.userName", is(equalTo("johndoe"))))
				.andExpect(jsonPath("$.user.firstName", is(equalTo("John"))))
				.andExpect(jsonPath("$.user.lastName", is(equalTo("Doe"))))
				.andExpect(jsonPath("$.user.email", is(equalTo("john.doe@mail.com"))))
				.andExpect(jsonPath("$.user.coach", is(equalTo(false))))
				.andExpect(jsonPath("$.user.academicDegree").doesNotExist())
				.andExpect(jsonPath("$.user.positionProfile").doesNotExist())
				.andExpect(jsonPath("$.user.summary").doesNotExist())
				.andExpect(jsonPath("$.user.specializations").doesNotExist())
				.andExpect(jsonPath("$.user.certificates").doesNotExist())
				.andExpect(jsonPath("$.user.languages").doesNotExist());
	}

}
