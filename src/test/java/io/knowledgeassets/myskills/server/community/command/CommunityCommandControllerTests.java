package io.knowledgeassets.myskills.server.community.command;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.Link;
import io.knowledgeassets.myskills.server.user.User;
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

@WebMvcTest(CommunityCommandController.class)
class CommunityCommandControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CommunityCommandService communityCommandService;

	@Test
	@DisplayName("Tests if a community can be created.")
	void testIfCommunityIsCreated() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		final ClassPathResource body = new ClassPathResource("community/create-community.json");
		final Community community = Community.builder()
				.title("Java User Group")
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
				.build();
		given(communityCommandService.create(community)).willReturn(
				Community.builder()
						.id("123")
						.title("Java User Group")
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
					.andExpect(jsonPath("$.description", is(equalTo("Community for Java developers"))))
					.andExpect(jsonPath("$.links[0].name", is(equalTo("Facebook"))))
					.andExpect(jsonPath("$.links[0].href", is(equalTo("https://www.facebook.com/java-user-group"))))
					.andExpect(jsonPath("$.links[1].name", is(equalTo("Linkedin"))))
					.andExpect(jsonPath("$.links[1].href", is(equalTo("https://www.linkedin.com/java-user-group"))));
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
		final ClassPathResource body = new ClassPathResource("community/update-community.json");
		final Community community = Community.builder()
				.id("123")
				.title("Java User Group")
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
				.build();
		given(communityCommandService.update(community)).willReturn(
				Community.builder()
						.id("123")
						.title("Java User Group")
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
					.andExpect(jsonPath("$.description", is(equalTo("New community for java developers"))))
					.andExpect(jsonPath("$.links[0].name", is(equalTo("Facebook"))))
					.andExpect(jsonPath("$.links[0].href", is(equalTo("https://www.facebook.com/java-user-group"))))
					.andExpect(jsonPath("$.links[1].name", is(equalTo("Linkedin"))))
					.andExpect(jsonPath("$.links[1].href", is(equalTo("https://www.linkedin.com/java-user-group"))));
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
	@DisplayName("Tests if the community can be deleted.")
	void testIfCommunityCanBeDeleted() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		mockMvc.perform(delete("/communities/123")
				.with(authentication(withUser(owner, "ADMIN"))))
				.andExpect(status().isNoContent());
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
