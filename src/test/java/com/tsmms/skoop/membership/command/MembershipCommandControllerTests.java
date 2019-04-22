package com.tsmms.skoop.membership.command;

import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.membership.Membership;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.query.SkillQueryService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MembershipCommandController.class)
class MembershipCommandControllerTests extends AbstractControllerTests {

	@MockBean
	private UserQueryService userQueryService;

	@MockBean
	private SkillQueryService skillQueryService;

	@MockBean
	private MembershipCommandService membershipCommandService;

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("Creates membership.")
	@Test
	void createMembership() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		final ClassPathResource body = new ClassPathResource("membership/create-membership.json");

		given(userQueryService.getUserById(tester.getId()))
				.willReturn(Optional.of(tester));

		given(skillQueryService.convertSkillNamesToSkills(Arrays.asList("Java", "Spring Boot")))
				.willReturn(
						Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.name("Spring Boot")
										.build()
						)
				);

		given(membershipCommandService.create(Membership.builder()
				.name("First membership")
				.description("First membership description")
				.link("http://first-link.com")
				.skills(Arrays.asList(
						Skill.builder()
								.id("123")
								.name("Java")
								.build(),
						Skill.builder()
								.name("Spring Boot")
								.build()
				))
				.user(User.builder()
						.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
						.userName("tester")
						.build())
				.build())
		)
				.willReturn(Membership.builder()
						.id("123")
						.name("First membership")
						.description("First membership description")
						.link("http://first-link.com")
						.skills(Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.id("456")
										.name("Spring Boot")
										.build()
						))
						.creationDatetime(LocalDateTime.of(2019, 4, 19, 13, 0))
						.lastModifiedDatetime(LocalDateTime.of(2019, 4, 19, 13, 0))
						.user(User.builder()
								.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
								.userName("tester")
								.build())
						.build()
				);

		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/memberships")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(tester)))
					.with(csrf()))
					.andExpect(status().isCreated())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.id", is(equalTo("123"))))
					.andExpect(jsonPath("$.name", is(equalTo("First membership"))))
					.andExpect(jsonPath("$.description", is(equalTo("First membership description"))))
					.andExpect(jsonPath("$.creationDatetime", is(equalTo("2019-04-19T13:00:00"))))
					.andExpect(jsonPath("$.lastModifiedDatetime", is(equalTo("2019-04-19T13:00:00"))))
					.andExpect(jsonPath("$.link", is(equalTo("http://first-link.com"))))
					.andExpect(jsonPath("$.skills[0].id", is(equalTo("123"))))
					.andExpect(jsonPath("$.skills[0].name", is(equalTo("Java"))))
					.andExpect(jsonPath("$.skills[1].id", is(equalTo("456"))))
					.andExpect(jsonPath("$.skills[1].name", is(equalTo("Spring Boot"))));
		}
	}

	@DisplayName("Not authenticated user cannot create memberships.")
	@Test
	void notAuthenticatedUserCannotCreateMemberships() throws Exception {
		final ClassPathResource body = new ClassPathResource("membership/create-membership.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/memberships")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(csrf()))
					.andExpect(status().isUnauthorized());
		}
	}

	@DisplayName("User cannot create memberships of other users.")
	@Test
	void userCannotCreateMembershipsOfOtherUsers() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		final ClassPathResource body = new ClassPathResource("membership/create-membership.json");

		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/users/c9cf7118-5f9e-40fc-9d89-28b2d0a77340/memberships")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(tester)))
					.with(csrf()))
					.andExpect(status().isForbidden());
		}
	}

	@DisplayName("Updates membership.")
	@Test
	void updateMembership() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		final ClassPathResource body = new ClassPathResource("membership/update-membership.json");

		given(skillQueryService.convertSkillNamesToSkills(Arrays.asList("Java", "Spring Boot", "Angular")))
				.willReturn(
						Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.id("456")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.name("Angular")
										.build()
						)
				);

		given(membershipCommandService.update("123", MembershipUpdateCommand.builder()
				.name("First membership updated")
				.description("First membership description updated")
				.link("http://first-updated-link.com")
				.skills(Arrays.asList(
						Skill.builder()
								.id("123")
								.name("Java")
								.build(),
						Skill.builder()
								.id("456")
								.name("Spring Boot")
								.build(),
						Skill.builder()
								.name("Angular")
								.build()
				))
				.build()
		)).willReturn(
				Membership.builder()
						.id("123")
						.name("First membership updated")
						.description("First membership description updated")
						.link("http://first-updated-link.com")
						.skills(Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.id("456")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.id("789")
										.name("Angular")
										.build()
						))
						.creationDatetime(LocalDateTime.of(2019, 4, 22, 13, 0))
						.lastModifiedDatetime(LocalDateTime.of(2019, 4, 22, 13, 0))
						.user(tester)
						.build()
		);

		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/memberships/123")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(tester)))
					.with(csrf()))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.id", is(equalTo("123"))))
					.andExpect(jsonPath("$.name", is(equalTo("First membership updated"))))
					.andExpect(jsonPath("$.description", is(equalTo("First membership description updated"))))
					.andExpect(jsonPath("$.creationDatetime", is(equalTo("2019-04-22T13:00:00"))))
					.andExpect(jsonPath("$.lastModifiedDatetime", is(equalTo("2019-04-22T13:00:00"))))
					.andExpect(jsonPath("$.link", is(equalTo("http://first-updated-link.com"))))
					.andExpect(jsonPath("$.skills[0].id", is(equalTo("123"))))
					.andExpect(jsonPath("$.skills[0].name", is(equalTo("Java"))))
					.andExpect(jsonPath("$.skills[1].id", is(equalTo("456"))))
					.andExpect(jsonPath("$.skills[1].name", is(equalTo("Spring Boot"))))
					.andExpect(jsonPath("$.skills[2].id", is(equalTo("789"))))
					.andExpect(jsonPath("$.skills[2].name", is(equalTo("Angular"))));
		}
	}

	@DisplayName("Not authenticated user cannot update memberships.")
	@Test
	void notAuthenticatedUserCannotUpdateMemberships() throws Exception {
		final ClassPathResource body = new ClassPathResource("membership/update-membership.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/memberships/123")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(csrf()))
					.andExpect(status().isUnauthorized());
		}
	}

	@DisplayName("User cannot update memberships of other users.")
	@Test
	void userCannotUpdateMembershipsOfOtherUsers() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		final ClassPathResource body = new ClassPathResource("membership/update-membership.json");

		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/users/c9cf7118-5f9e-40fc-9d89-28b2d0a77340/memberships/123")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(tester)))
					.with(csrf()))
					.andExpect(status().isForbidden());
		}
	}

	@DisplayName("Deletes membership.")
	@Test
	void deletesMembership() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();
		mockMvc.perform(delete("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/memberships/123")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(tester)))
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@DisplayName("Not authenticated user cannot delete membership.")
	@Test
	void notAuthenticatedUserCannotDeleteMemberships() throws Exception {
		mockMvc.perform(delete("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/memberships/123")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

	@DisplayName("User cannot delete membership of other users.")
	@Test
	void userCannotDeleteMembershipsOfOtherUsers() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();
		mockMvc.perform(delete("/users/c9cf7118-5f9e-40fc-9d89-28b2d0a77340/memberships/123")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(tester)))
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

}
