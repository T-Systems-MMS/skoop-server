package io.knowledgeassets.myskills.server.community.command;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.community.Link;
import io.knowledgeassets.myskills.server.security.CurrentUserService;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryService;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
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
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static io.knowledgeassets.myskills.server.common.JwtAuthenticationFactory.withUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static java.util.Collections.singletonList;

@WebMvcTest(CommunityCommandController.class)
class CommunityCommandControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CommunityCommandService communityCommandService;

	@MockBean
	private UserQueryService userQueryService;

	@MockBean
	private CurrentUserService currentUserService;

	@MockBean
	private SkillQueryService skillQueryService;

	@Test
	@DisplayName("Tests if a community can be created.")
	void testIfCommunityIsCreated() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		final ClassPathResource body = new ClassPathResource("community/create-community.json");

		given(skillQueryService.findByNameIgnoreCase("Spring Boot")).willReturn(
				Optional.of(
						Skill.builder()
								.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
								.name("Spring Boot")
								.build()
				)
		);

		given(skillQueryService.findByNameIgnoreCase("Angular")).willReturn(
				Optional.of(
						Skill.builder()
								.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
								.name("Angular")
								.build()
				)
		);

		final Community community = Community.builder()
				.title("Java User Group")
				.type(CommunityType.OPENED)
				.description("Community for Java developers")
				.links(Arrays.asList(
						Link.builder()
								.name("Facebook")
								.href("https://www.facebook.com/java-user-group")
								.build(),
						Link.builder()
								.name("Linkedin")
								.href("https://www.linkedin.com/java-user-group")
								.build()
				))
				.skills(Arrays.asList(Skill.builder()
						.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
						.name("Spring Boot")
						.build(),
						Skill.builder()
						.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
						.name("Angular")
						.build(),
						Skill.builder()
						.name("Spring MVC")
						.build()
						))
				.build();
		given(communityCommandService.create(community)).willReturn(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPENED)
						.description("Community for Java developers")
						.links(Arrays.asList(
								Link.builder()
										.name("Facebook")
										.href("https://www.facebook.com/java-user-group")
										.build(),
								Link.builder()
										.name("Linkedin")
										.href("https://www.linkedin.com/java-user-group")
										.build()
						))
						.managers(singletonList(owner))
						.members(singletonList(owner))
						.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
						.skills(Arrays.asList(Skill.builder()
										.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
										.name("Angular")
										.build(),
								Skill.builder()
										.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
										.name("Spring MVC")
										.build()))
						.build()
		);
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/communities")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isCreated())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.id", is(equalTo("123"))))
					.andExpect(jsonPath("$.title", is(equalTo("Java User Group"))))
					.andExpect(jsonPath("$.type", is(equalTo("OPENED"))))
					.andExpect(jsonPath("$.description", is(equalTo("Community for Java developers"))))
					.andExpect(jsonPath("$.links[0].name", is(equalTo("Facebook"))))
					.andExpect(jsonPath("$.links[0].href", is(equalTo("https://www.facebook.com/java-user-group"))))
					.andExpect(jsonPath("$.links[1].name", is(equalTo("Linkedin"))))
					.andExpect(jsonPath("$.links[1].href", is(equalTo("https://www.linkedin.com/java-user-group"))))
					.andExpect(jsonPath("$.managers[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
					.andExpect(jsonPath("$.managers[0].userName", is(equalTo("tester"))))
					.andExpect(jsonPath("$.members[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
					.andExpect(jsonPath("$.members[0].userName", is(equalTo("tester"))))
					.andExpect(jsonPath("$.skills[0].id", is(equalTo("4f09647e-c7d3-4aa6-ab3d-0faff66b951f"))))
					.andExpect(jsonPath("$.skills[0].name", is(equalTo("Spring Boot"))))
					.andExpect(jsonPath("$.skills[1].id", is(equalTo("6d0870d0-a7b8-4cf4-8a24-bedcfe350903"))))
					.andExpect(jsonPath("$.skills[1].name", is(equalTo("Angular"))))
					.andExpect(jsonPath("$.skills[2].id", is(equalTo("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3"))))
					.andExpect(jsonPath("$.skills[2].name", is(equalTo("Spring MVC"))));
		}
	}

	@Test
	@DisplayName("Tests if not authorized status code is returned when community is created by not authenticated user.")
	void testIfNotAuthorizedStatusCodeIsReturnedWhenCommunityIsCreatedByNotAuthenticatedUser() throws Exception {
		final ClassPathResource body = new ClassPathResource("community/create-community.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/communities")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(csrf()))
					.andExpect(status().isUnauthorized());
		}
	}

	@Test
	@DisplayName("Tests if bad request status code is returned in case community has an empty name when creating a community.")
	void testIfBadRequestStatusCodeIsReturnedInCaseCommunityHasEmptyNameWhenCreatingProject() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		final ClassPathResource body = new ClassPathResource("community/create-community-with-empty-name.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/communities")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isBadRequest());
		}
	}

	@Test
	@DisplayName("Tests if bad request status code is returned in case community has an empty name when updating a community.")
	void testIfBadRequestStatusCodeIsReturnedInCaseCommunityHasEmptyNameWhenUpdatingCommunity() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		final ClassPathResource body = new ClassPathResource("community/update-community-with-empty-name.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isBadRequest());
		}
	}

	@Test
	@DisplayName("Tests if a community can be updated.")
	void testIfCommunityIsUpdated() throws Exception {
		given(communityQueryService.hasCommunityManagerRole("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123")).willReturn(true);
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(currentUserService.getCurrentUser()).willReturn(owner);

		given(skillQueryService.findByNameIgnoreCase("Spring Boot")).willReturn(
				Optional.of(
						Skill.builder()
								.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
								.name("Spring Boot")
								.build()
				)
		);

		given(skillQueryService.findByNameIgnoreCase("Angular")).willReturn(
				Optional.of(
						Skill.builder()
								.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
								.name("Angular")
								.build()
				)
		);

		given(userQueryService.getUsersByIds(singletonList("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))).willReturn(Stream.of(owner), Stream.of(owner));
		final ClassPathResource body = new ClassPathResource("community/update-community.json");
		final Community community = Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPENED)
				.description("New community for Java developers")
				.links(Arrays.asList(
						Link.builder()
								.name("Facebook")
								.href("https://www.facebook.com/java-user-group")
								.build(),
						Link.builder()
								.name("Linkedin")
								.href("https://www.linkedin.com/java-user-group")
								.build()
				))
				.managers(singletonList(owner))
				.members(singletonList(owner))
				.skills(Arrays.asList(Skill.builder()
								.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
								.name("Spring Boot")
								.build(),
						Skill.builder()
								.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
								.name("Angular")
								.build(),
						Skill.builder()
								.name("Spring MVC")
								.build()))
				.build();
		given(communityCommandService.update(community)).willReturn(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPENED)
						.description("New community for java developers")
						.links(Arrays.asList(
								Link.builder()
										.name("Facebook")
										.href("https://www.facebook.com/java-user-group")
										.build(),
								Link.builder()
										.name("Linkedin")
										.href("https://www.linkedin.com/java-user-group")
										.build()
						))
						.managers(singletonList(owner))
						.members(singletonList(owner))
						.skills(Arrays.asList(Skill.builder()
								.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
								.name("Spring Boot")
								.build(),
								Skill.builder()
										.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
										.name("Angular")
										.build(),
								Skill.builder()
										.id("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3")
										.name("Spring MVC")
										.build()))
						.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
						.build()
		);
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.id", is(equalTo("123"))))
					.andExpect(jsonPath("$.title", is(equalTo("Java User Group"))))
					.andExpect(jsonPath("$.type", is(equalTo("OPENED"))))
					.andExpect(jsonPath("$.description", is(equalTo("New community for java developers"))))
					.andExpect(jsonPath("$.links[0].name", is(equalTo("Facebook"))))
					.andExpect(jsonPath("$.links[0].href", is(equalTo("https://www.facebook.com/java-user-group"))))
					.andExpect(jsonPath("$.links[1].name", is(equalTo("Linkedin"))))
					.andExpect(jsonPath("$.links[1].href", is(equalTo("https://www.linkedin.com/java-user-group"))))
					.andExpect(jsonPath("$.managers[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
					.andExpect(jsonPath("$.managers[0].userName", is(equalTo("tester"))))
					.andExpect(jsonPath("$.members[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
					.andExpect(jsonPath("$.members[0].userName", is(equalTo("tester"))))
					.andExpect(jsonPath("$.skills[0].id", is(equalTo("4f09647e-c7d3-4aa6-ab3d-0faff66b951f"))))
					.andExpect(jsonPath("$.skills[0].name", is(equalTo("Spring Boot"))))
					.andExpect(jsonPath("$.skills[1].id", is(equalTo("6d0870d0-a7b8-4cf4-8a24-bedcfe350903"))))
					.andExpect(jsonPath("$.skills[1].name", is(equalTo("Angular"))))
					.andExpect(jsonPath("$.skills[2].id", is(equalTo("dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3"))))
					.andExpect(jsonPath("$.skills[2].name", is(equalTo("Spring MVC"))));
		}
	}

	@Test
	@DisplayName("Tests if a community cannot be updated when community manager removes community manager role from himself.")
	void testIfCommunityCannotBeUpdatedWhenCommunityManagerRemovesCommunityManagerRoleFromHimself() throws Exception {
		given(communityQueryService.hasCommunityManagerRole("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123")).willReturn(true);
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(currentUserService.getCurrentUser()).willReturn(owner);
		final ClassPathResource body = new ClassPathResource("community/update-community-with-community-manager-removed.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isBadRequest());
		}
	}

	@Test
	@DisplayName("Tests if a community cannot be updated when community manager removes member role from himself.")
	void testIfCommunityCannotBeUpdatedWhenCommunityManagerRemovesMemberRoleFromHimself() throws Exception {
		given(communityQueryService.hasCommunityManagerRole("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123")).willReturn(true);
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(currentUserService.getCurrentUser()).willReturn(owner);
		final ClassPathResource body = new ClassPathResource("community/update-community-with-community-members-removed.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(owner)))
					.with(csrf()))
					.andExpect(status().isBadRequest());
		}
	}

	@Test
	@DisplayName("Tests if not authorized status code is returned when community is updated by not authenticated user.")
	void testIfNotAuthorizedStatusCodeIsReturnedWhenCommunityIsUpdatedByNotAuthenticatedUser() throws Exception {
		final ClassPathResource body = new ClassPathResource("community/update-community.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(csrf()))
					.andExpect(status().isUnauthorized());
		}
	}

	@Test
	@DisplayName("Tests if forbbiden status code is returned when community is updated by a user who is not a community manager.")
	void testIfForbiddenStatusCodeIsReturnedWhenCommunityIsUpdatedByUserWhoIsNotCommunityManager() throws Exception {
		given(communityQueryService.hasCommunityManagerRole("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123")).willReturn(false);
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		final ClassPathResource body = new ClassPathResource("community/update-community.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/communities/123")
					.with(authentication(withUser(owner)))
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(csrf()))
					.andExpect(status().isForbidden());
		}
	}

	@Test
	@DisplayName("Tests if the community can be deleted.")
	void testIfCommunityCanBeDeleted() throws Exception {
		given(communityQueryService.hasCommunityManagerRole("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123")).willReturn(true);
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		mockMvc.perform(delete("/communities/123")
				.with(authentication(withUser(owner))))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Tests if the community cannot be deleted by a user who is not a community manager.")
	void testIfCommunityCannotBeDeletedByUserWhoIsNotCommunityManager() throws Exception {
		given(communityQueryService.hasCommunityManagerRole("1f37fb2a-b4d0-4119-9113-4677beb20ae2", "123")).willReturn(false);
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		mockMvc.perform(delete("/communities/123")
				.with(authentication(withUser(owner, "ADMIN"))))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Tests if the community cannot be deleted by unauthorized user.")
	void testIfCommunityCannotBeDeletedByUnauthorizedUser() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		mockMvc.perform(delete("/communities/123")
				.with(authentication(withUser(owner))))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Tests if not authorized status code is returned when community is deleted by not authenticated user.")
	void testIfNotAuthorizedStatusCodeIsReturnedWhenCommunityIsDeletedByNotAuthenticatedUser() throws Exception {
		mockMvc.perform(delete("/communities/123"))
				.andExpect(status().isUnauthorized());
	}

}
