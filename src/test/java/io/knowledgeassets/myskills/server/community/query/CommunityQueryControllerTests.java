package io.knowledgeassets.myskills.server.community.query;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.CommunityType;
import io.knowledgeassets.myskills.server.community.Link;
import io.knowledgeassets.myskills.server.community.RecommendedCommunity;
import io.knowledgeassets.myskills.server.security.CurrentUserService;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static io.knowledgeassets.myskills.server.common.JwtAuthenticationFactory.withUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static java.util.Collections.singletonList;

@WebMvcTest(CommunityQueryController.class)
class CommunityQueryControllerTests extends AbstractControllerTests {

	@MockBean
	private CurrentUserService currentUserService;

	@MockBean
	private CommunityQueryService communityQueryService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Tests if all communities can be fetched.")
	void testIfAllCommunitiesAreFetched() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		final Skill springBootSkill = Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build();

		final Skill angularSkill = Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build();

		final Skill javascriptSkill = Skill.builder()
				.id("10ea2af6-cd81-48e0-b339-0576d16b9d19")
				.name("JavaScript")
				.build();

		given(currentUserService.getCurrentUser()).willReturn(owner);

		given(communityQueryService.getCommunitiesRecommendedForUser("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(
				Stream.of(
						RecommendedCommunity.builder()
								.community(
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
												.skills(Arrays.asList(springBootSkill, angularSkill))
												.build()
								)
							.recommended(true)
						.build(),
						RecommendedCommunity.builder()
						.community(
								Community.builder()
										.id("456")
										.title("Scala User Group")
										.type(CommunityType.OPENED)
										.description("Community for Scala developers")
										.links(Arrays.asList(
												Link.builder()
														.name("Facebook")
														.href("https://www.facebook.com/scala-user-group")
														.build(),
												Link.builder()
														.name("Linkedin")
														.href("https://www.linkedin.com/scala-user-group")
														.build()
										))
										.managers(singletonList(owner))
										.members(singletonList(owner))
										.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
										.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
										.skills(Arrays.asList(springBootSkill, javascriptSkill))
										.build()
						)
						.recommended(false)
						.build()
				)
		);
		mockMvc.perform(get("/communities")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner)))
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id", is(equalTo("123"))))
				.andExpect(jsonPath("$[0].title", is(equalTo("Java User Group"))))
				.andExpect(jsonPath("$[0].type", is(equalTo("OPENED"))))
				.andExpect(jsonPath("$[0].description", is(equalTo("Community for Java developers"))))
				.andExpect(jsonPath("$[0].links[0].name", is(equalTo("Facebook"))))
				.andExpect(jsonPath("$[0].links[0].href", is(equalTo("https://www.facebook.com/java-user-group"))))
				.andExpect(jsonPath("$[0].links[1].name", is(equalTo("Linkedin"))))
				.andExpect(jsonPath("$[0].links[1].href", is(equalTo("https://www.linkedin.com/java-user-group"))))
				.andExpect(jsonPath("$[0].managers[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[0].managers[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].members[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[0].members[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].skills[0].id", is(equalTo("4f09647e-c7d3-4aa6-ab3d-0faff66b951f"))))
				.andExpect(jsonPath("$[0].skills[0].name", is(equalTo("Spring Boot"))))
				.andExpect(jsonPath("$[0].skills[1].id", is(equalTo("6d0870d0-a7b8-4cf4-8a24-bedcfe350903"))))
				.andExpect(jsonPath("$[0].skills[1].name", is(equalTo("Angular"))))
				.andExpect(jsonPath("$[0].recommended", is(true)))
				.andExpect(jsonPath("$[1].id", is(equalTo("456"))))
				.andExpect(jsonPath("$[1].title", is(equalTo("Scala User Group"))))
				.andExpect(jsonPath("$[1].type", is(equalTo("OPENED"))))
				.andExpect(jsonPath("$[1].description", is(equalTo("Community for Scala developers"))))
				.andExpect(jsonPath("$[1].links[0].name", is(equalTo("Facebook"))))
				.andExpect(jsonPath("$[1].links[0].href", is(equalTo("https://www.facebook.com/scala-user-group"))))
				.andExpect(jsonPath("$[1].links[1].name", is(equalTo("Linkedin"))))
				.andExpect(jsonPath("$[1].links[1].href", is(equalTo("https://www.linkedin.com/scala-user-group"))))
				.andExpect(jsonPath("$[1].managers[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[1].managers[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[1].members[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[1].members[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[1].skills[0].id", is(equalTo("4f09647e-c7d3-4aa6-ab3d-0faff66b951f"))))
				.andExpect(jsonPath("$[1].skills[0].name", is(equalTo("Spring Boot"))))
				.andExpect(jsonPath("$[1].skills[1].id", is(equalTo("10ea2af6-cd81-48e0-b339-0576d16b9d19"))))
				.andExpect(jsonPath("$[1].skills[1].name", is(equalTo("JavaScript"))))
				.andExpect(jsonPath("$[1].recommended", is(false)));
	}

	@Test
	@DisplayName("Tests if additional properties are not retrieved for the communities a requesting user is not a member of.")
	void testIfAdditionalPropertiesAreNotRetrievedForCommunitiesRequestingUserIsNotMemberOf() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		final Skill springBootSkill = Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build();

		final Skill angularSkill = Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build();

		final Skill javascriptSkill = Skill.builder()
				.id("10ea2af6-cd81-48e0-b339-0576d16b9d19")
				.name("JavaScript")
				.build();

		given(currentUserService.getCurrentUser()).willReturn(owner);

		given(communityQueryService.getCommunitiesRecommendedForUser("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(
				Stream.of(
						RecommendedCommunity.builder()
						.community(
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
										.skills(Arrays.asList(springBootSkill, angularSkill))
										.build()
						)
						.recommended(true)
						.build(),
						RecommendedCommunity.builder()
						.community(
								Community.builder()
										.id("456")
										.title("Scala User Group")
										.type(CommunityType.OPENED)
										.description("Community for Scala developers")
										.links(Arrays.asList(
												Link.builder()
														.name("Facebook")
														.href("https://www.facebook.com/scala-user-group")
														.build(),
												Link.builder()
														.name("Linkedin")
														.href("https://www.linkedin.com/scala-user-group")
														.build()
										))
										.managers(singletonList(User.builder()
												.id("a396d5a8-a6b1-4c71-9498-a16e655dae2e")
												.userName("anotherUser")
												.build()))
										.members(singletonList(User.builder()
												.id("a396d5a8-a6b1-4c71-9498-a16e655dae2e")
												.userName("anotherUser")
												.build()))
										.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
										.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
										.skills(Arrays.asList(springBootSkill, javascriptSkill))
										.build()
						)
						.recommended(false)
						.build()
				)
		);
		mockMvc.perform(get("/communities")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner)))
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id", is(equalTo("123"))))
				.andExpect(jsonPath("$[0].title", is(equalTo("Java User Group"))))
				.andExpect(jsonPath("$[0].type", is(equalTo("OPENED"))))
				.andExpect(jsonPath("$[0].description", is(equalTo("Community for Java developers"))))
				.andExpect(jsonPath("$[0].links[0].name", is(equalTo("Facebook"))))
				.andExpect(jsonPath("$[0].links[0].href", is(equalTo("https://www.facebook.com/java-user-group"))))
				.andExpect(jsonPath("$[0].links[1].name", is(equalTo("Linkedin"))))
				.andExpect(jsonPath("$[0].links[1].href", is(equalTo("https://www.linkedin.com/java-user-group"))))
				.andExpect(jsonPath("$[0].managers[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[0].managers[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].members[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[0].members[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].skills[0].id", is(equalTo("4f09647e-c7d3-4aa6-ab3d-0faff66b951f"))))
				.andExpect(jsonPath("$[0].skills[0].name", is(equalTo("Spring Boot"))))
				.andExpect(jsonPath("$[0].skills[1].id", is(equalTo("6d0870d0-a7b8-4cf4-8a24-bedcfe350903"))))
				.andExpect(jsonPath("$[0].skills[1].name", is(equalTo("Angular"))))
				.andExpect(jsonPath("$[0].recommended", is(true)))
				.andExpect(jsonPath("$[1].id", is(equalTo("456"))))
				.andExpect(jsonPath("$[1].title", is(equalTo("Scala User Group"))))
				.andExpect(jsonPath("$[1].type", is(equalTo("OPENED"))))
				.andExpect(jsonPath("$[1].description", is(equalTo("Community for Scala developers"))))
				.andExpect(jsonPath("$[1].links[0].name", is(equalTo("Facebook"))))
				.andExpect(jsonPath("$[1].links[0].href", is(equalTo("https://www.facebook.com/scala-user-group"))))
				.andExpect(jsonPath("$[1].links[1].name", is(equalTo("Linkedin"))))
				.andExpect(jsonPath("$[1].links[1].href", is(equalTo("https://www.linkedin.com/scala-user-group"))))
				.andExpect(jsonPath("$[1].managers[0].id", is(equalTo("a396d5a8-a6b1-4c71-9498-a16e655dae2e"))))
				.andExpect(jsonPath("$[1].managers[0].userName", is(equalTo("anotherUser"))))
				.andExpect(jsonPath("$[1].members", nullValue()))
				.andExpect(jsonPath("$[1].skills[0].id", is(equalTo("4f09647e-c7d3-4aa6-ab3d-0faff66b951f"))))
				.andExpect(jsonPath("$[1].skills[0].name", is(equalTo("Spring Boot"))))
				.andExpect(jsonPath("$[1].skills[1].id", is(equalTo("10ea2af6-cd81-48e0-b339-0576d16b9d19"))))
				.andExpect(jsonPath("$[1].skills[1].name", is(equalTo("JavaScript"))))
				.andExpect(jsonPath("$[1].recommended", is(false)));
	}

	@Test
	@DisplayName("Tests if a community can be fetched by identifier.")
	void testIfCommunityIsFetchedByIdentifier() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		final Skill springBootSkill = Skill.builder()
				.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
				.name("Spring Boot")
				.build();

		final Skill angularSkill = Skill.builder()
				.id("6d0870d0-a7b8-4cf4-8a24-bedcfe350903")
				.name("Angular")
				.build();

		given(currentUserService.getCurrentUser()).willReturn(owner);

		given(communityQueryService.getCommunityById("456")).willReturn(
				Optional.of(
						Community.builder()
								.id("456")
								.title("Scala User Group")
								.type(CommunityType.OPENED)
								.description("Community for Scala developers")
								.links(Arrays.asList(
										Link.builder()
												.name("Facebook")
												.href("https://www.facebook.com/scala-user-group")
												.build(),
										Link.builder()
												.name("Linkedin")
												.href("https://www.linkedin.com/scala-user-group")
												.build()
								))
								.managers(singletonList(owner))
								.members(singletonList(owner))
								.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
								.skills(Arrays.asList(springBootSkill, angularSkill))
								.build()
				)
		);
		mockMvc.perform(get("/communities/456")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner)))
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("456"))))
				.andExpect(jsonPath("$.title", is(equalTo("Scala User Group"))))
				.andExpect(jsonPath("$.type", is(equalTo("OPENED"))))
				.andExpect(jsonPath("$.description", is(equalTo("Community for Scala developers"))))
				.andExpect(jsonPath("$.links[0].name", is(equalTo("Facebook"))))
				.andExpect(jsonPath("$.links[0].href", is(equalTo("https://www.facebook.com/scala-user-group"))))
				.andExpect(jsonPath("$.links[1].name", is(equalTo("Linkedin"))))
				.andExpect(jsonPath("$.links[1].href", is(equalTo("https://www.linkedin.com/scala-user-group"))))
				.andExpect(jsonPath("$.managers[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$.managers[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$.members[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$.members[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$.skills[0].id", is(equalTo("4f09647e-c7d3-4aa6-ab3d-0faff66b951f"))))
				.andExpect(jsonPath("$.skills[0].name", is(equalTo("Spring Boot"))))
				.andExpect(jsonPath("$.skills[1].id", is(equalTo("6d0870d0-a7b8-4cf4-8a24-bedcfe350903"))))
				.andExpect(jsonPath("$.skills[1].name", is(equalTo("Angular"))));
	}

	@Test
	@DisplayName("Tests if not found status code is returned in case community was not found by identifier.")
	void testIfNotFoundStatusCodeIsReturnedInCaseCommunityWasNotFoundByIdentifier() throws Exception {
		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();
		given(communityQueryService.getCommunityById("456")).willReturn(Optional.empty());
		mockMvc.perform(get("/communities/456")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner)))
				.with(csrf()))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	@DisplayName("Tests if not authorized status code is returned in case community cannot be fetched by not authenticated user.")
	void testIfNotAuthorizedStatusCodeIsReturnedInCaseCommunityCannotBeFetchedByNotAuthenticatedUser() throws Exception {
		mockMvc.perform(get("/communities/456")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("Tests if not authorized status code is returned in case all communities cannot be fetched by not authenticated user.")
	void testIfNotAuthorizedStatusCodeIsReturnedInCaseAllCommunitiesCannotBeFetchedByNotAuthenticatedUser() throws Exception {
		mockMvc.perform(get("/communities")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

}
