package com.tsmms.skoop.community.command;

import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityRole;
import com.tsmms.skoop.community.CommunityType;
import com.tsmms.skoop.community.link.Link;
import com.tsmms.skoop.community.query.CommunityQueryService;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.query.SkillQueryService;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.communityuser.CommunityUser;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;
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
	private SkillQueryService skillQueryService;

	@MockBean
	private CommunityQueryService communityQueryService;

	@Test
	@DisplayName("Tests if a community can be created.")
	void testIfCommunityIsCreated() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		final ClassPathResource body = new ClassPathResource("community/create-community.json");

		given(skillQueryService.convertSkillNamesToSkillsSet(new HashSet<>(Arrays.asList("Spring Boot", "Angular", "Spring MVC")))).willReturn(
				new HashSet<>(
						Arrays.asList(
								Skill.builder()
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
						)
				)
		);

		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))
				.willReturn(Optional.of(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("firstTester")
						.build()
				));

		given(userQueryService.getUserById("d9d74c04-0ab0-479c-a1d7-d372990f11b6"))
				.willReturn(Optional.of(User.builder()
						.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
						.userName("secondTester")
						.build()
				));

		final Community community = Community.builder()
				.title("Java User Group")
				.type(CommunityType.OPEN)
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
				.skills(new HashSet<>(
						Arrays.asList(Skill.builder()
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
						)
				))
				.build();
		given(communityCommandService.create(community, Arrays.asList(
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("firstTester")
						.build(),
				User.builder()
						.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
						.userName("secondTester")
						.build()
		))).willReturn(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
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
						.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
						.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
						.skills(new HashSet<>(
								Arrays.asList(Skill.builder()
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
												.build())
						))
						.communityUsers(singletonList(CommunityUser.builder()
								.user(owner)
								.role(CommunityRole.MANAGER)
								.build()
						))
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
					.andExpect(jsonPath("$.type", is(equalTo("OPEN"))))
					.andExpect(jsonPath("$.description", is(equalTo("Community for Java developers"))))
					.andExpect(jsonPath("$.links[0].name", is(equalTo("Facebook"))))
					.andExpect(jsonPath("$.links[0].href", is(equalTo("https://www.facebook.com/java-user-group"))))
					.andExpect(jsonPath("$.links[1].name", is(equalTo("Linkedin"))))
					.andExpect(jsonPath("$.links[1].href", is(equalTo("https://www.linkedin.com/java-user-group"))))
					.andExpect(jsonPath("$.managers[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
					.andExpect(jsonPath("$.managers[0].userName", is(equalTo("tester"))))
					.andExpect(jsonPath("$.skills[0].id", is(equalTo("4f09647e-c7d3-4aa6-ab3d-0faff66b951f"))))
					.andExpect(jsonPath("$.skills[0].name", is(equalTo("Spring Boot"))))
					.andExpect(jsonPath("$.skills[?(@.id=='6d0870d0-a7b8-4cf4-8a24-bedcfe350903')].name", hasItem("Angular")))
					.andExpect(jsonPath("$.skills[?(@.id=='dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3')].name", hasItem("Spring MVC")));
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

		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.isCommunityManager(owner.getId(), "123")).willReturn(true);

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

		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))
				.willReturn(Optional.of(User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("firstTester")
						.build()
				));

		given(userQueryService.getUserById("d9d74c04-0ab0-479c-a1d7-d372990f11b6"))
				.willReturn(Optional.of(User.builder()
						.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
						.userName("secondTester")
						.build()
				));

		given(userQueryService.getUsersByIds(singletonList("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))).willReturn(Stream.of(owner), Stream.of(owner));
		final ClassPathResource body = new ClassPathResource("community/update-community.json");
		final Community community = Community.builder()
				.id("123")
				.title("Java User Group")
				.type(CommunityType.OPEN)
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
				.skills(new HashSet<>(
						Arrays.asList(Skill.builder()
										.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
										.name("Spring Boot")
										.build(),
								Skill.builder()
										.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
										.name("Angular")
										.build(),
								Skill.builder()
										.name("Spring MVC")
										.build())
				))
				.build();
		given(communityCommandService.update(community)).willReturn(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.type(CommunityType.OPEN)
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
						.skills(new HashSet<>(
								Arrays.asList(Skill.builder()
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
												.build())
						))
						.communityUsers(singletonList(CommunityUser.builder()
								.user(owner)
								.role(CommunityRole.MANAGER)
								.build()
						))
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
					.andExpect(jsonPath("$.type", is(equalTo("OPEN"))))
					.andExpect(jsonPath("$.description", is(equalTo("New community for java developers"))))
					.andExpect(jsonPath("$.links[0].name", is(equalTo("Facebook"))))
					.andExpect(jsonPath("$.links[0].href", is(equalTo("https://www.facebook.com/java-user-group"))))
					.andExpect(jsonPath("$.links[1].name", is(equalTo("Linkedin"))))
					.andExpect(jsonPath("$.links[1].href", is(equalTo("https://www.linkedin.com/java-user-group"))))
					.andExpect(jsonPath("$.managers[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
					.andExpect(jsonPath("$.managers[0].userName", is(equalTo("tester"))))
					.andExpect(jsonPath("$.skills[?(@.id=='4f09647e-c7d3-4aa6-ab3d-0faff66b951f')].name", hasItem("Spring Boot")))
					.andExpect(jsonPath("$.skills[?(@.id=='6d0870d0-a7b8-4cf4-8a24-bedcfe350903')].name", hasItem("Angular")))
					.andExpect(jsonPath("$.skills[?(@.id=='dce8b8c9-cd49-4a87-8cd2-4ca106dcf7f3')].name", hasItem("Spring MVC")));
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
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(communityQueryService.isCommunityManager(owner.getId(), "123")).willReturn(false);
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
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(communityQueryService.isCommunityManager(owner.getId(), "123")).willReturn(true);
		mockMvc.perform(delete("/communities/123")
				.with(authentication(withUser(owner)))
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Tests if the community cannot be deleted by a user who is not a community manager.")
	void testIfCommunityCannotBeDeletedByUserWhoIsNotCommunityManager() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(communityQueryService.isCommunityManager(owner.getId(), "123")).willReturn(false);
		mockMvc.perform(delete("/communities/123")
				.with(authentication(withUser(owner, "ADMIN")))
				.with(csrf()))
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
				.with(authentication(withUser(owner)))
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Tests if not authorized status code is returned when community is deleted by not authenticated user.")
	void testIfNotAuthorizedStatusCodeIsReturnedWhenCommunityIsDeletedByNotAuthenticatedUser() throws Exception {
		mockMvc.perform(delete("/communities/123")
				.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

}
