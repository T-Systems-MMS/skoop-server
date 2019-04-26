package com.tsmms.skoop.user.community;

import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.common.JwtAuthenticationFactory;
import com.tsmms.skoop.community.Community;
import com.tsmms.skoop.community.CommunityRole;
import com.tsmms.skoop.community.CommunityType;
import com.tsmms.skoop.community.link.Link;
import com.tsmms.skoop.community.query.CommunityQueryService;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.communityuser.CommunityUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserCommunityController.class)
class UserCommunityControllerTests extends AbstractControllerTests {

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

		given(communityQueryService.getCommunitiesRecommendedForUser("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(
				Stream.of(
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
								.communityUsers(singletonList(CommunityUser.builder()
										.role(CommunityRole.MANAGER)
										.user(owner)
										.build()))
								.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
								.skills(new HashSet<>(Arrays.asList(springBootSkill, angularSkill)))
								.build(),
						Community.builder()
								.id("456")
								.title("Scala User Group")
								.type(CommunityType.OPEN)
								.description("Community for Scala developers")
								.communityUsers(singletonList(CommunityUser.builder()
										.role(CommunityRole.MANAGER)
										.user(owner)
										.build()))
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
								.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
								.skills(new HashSet<>(Arrays.asList(springBootSkill, javascriptSkill)))
								.build()
				)
		);
		mockMvc.perform(get("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/community-recommendations")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(JwtAuthenticationFactory.withUser(owner)))
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id", is(equalTo("123"))))
				.andExpect(jsonPath("$[0].title", is(equalTo("Java User Group"))))
				.andExpect(jsonPath("$[0].type", is(equalTo("OPEN"))))
				.andExpect(jsonPath("$[0].description", is(equalTo("Community for Java developers"))))
				.andExpect(jsonPath("$[0].links[0].name", is(equalTo("Facebook"))))
				.andExpect(jsonPath("$[0].links[0].href", is(equalTo("https://www.facebook.com/java-user-group"))))
				.andExpect(jsonPath("$[0].links[1].name", is(equalTo("Linkedin"))))
				.andExpect(jsonPath("$[0].links[1].href", is(equalTo("https://www.linkedin.com/java-user-group"))))
				.andExpect(jsonPath("$[0].managers[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[0].managers[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].skills[0].id", is(equalTo("4f09647e-c7d3-4aa6-ab3d-0faff66b951f"))))
				.andExpect(jsonPath("$[0].skills[0].name", is(equalTo("Spring Boot"))))
				.andExpect(jsonPath("$[0].skills[1].id", is(equalTo("6d0870d0-a7b8-4cf4-8a24-bedcfe350903"))))
				.andExpect(jsonPath("$[0].skills[1].name", is(equalTo("Angular"))))
				.andExpect(jsonPath("$[1].id", is(equalTo("456"))))
				.andExpect(jsonPath("$[1].title", is(equalTo("Scala User Group"))))
				.andExpect(jsonPath("$[1].type", is(equalTo("OPEN"))))
				.andExpect(jsonPath("$[1].description", is(equalTo("Community for Scala developers"))))
				.andExpect(jsonPath("$[1].links[0].name", is(equalTo("Facebook"))))
				.andExpect(jsonPath("$[1].links[0].href", is(equalTo("https://www.facebook.com/scala-user-group"))))
				.andExpect(jsonPath("$[1].links[1].name", is(equalTo("Linkedin"))))
				.andExpect(jsonPath("$[1].links[1].href", is(equalTo("https://www.linkedin.com/scala-user-group"))))
				.andExpect(jsonPath("$[1].managers[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[1].managers[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[1].skills[0].id", is(equalTo("4f09647e-c7d3-4aa6-ab3d-0faff66b951f"))))
				.andExpect(jsonPath("$[1].skills[0].name", is(equalTo("Spring Boot"))))
				.andExpect(jsonPath("$[1].skills[1].id", is(equalTo("10ea2af6-cd81-48e0-b339-0576d16b9d19"))))
				.andExpect(jsonPath("$[1].skills[1].name", is(equalTo("JavaScript"))));
	}

	@DisplayName("Communities user takes part in are fetched.")
	@Test
	void userCommunitiesAreFetched() throws Exception {

		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(communityQueryService.getUserCommunities("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(
				Stream.of(
						Community.builder()
								.id("123")
								.title("Java User Group")
								.type(CommunityType.OPEN)
								.description("Community for Java developers")
								.communityUsers(singletonList(CommunityUser.builder()
										.role(CommunityRole.MANAGER)
										.user(owner)
										.build()))
								.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
								.skills(singleton(Skill.builder()
										.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
										.name("Spring Boot")
										.build()))
								.build(),
						Community.builder()
								.id("456")
								.title("Scala User Group")
								.type(CommunityType.OPEN)
								.description("Community for Scala developers")
								.communityUsers(singletonList(CommunityUser.builder()
										.role(CommunityRole.MANAGER)
										.user(owner)
										.build()))
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
								.creationDate(LocalDateTime.of(2019, 1, 9, 10, 30))
								.lastModifiedDate(LocalDateTime.of(2019, 1, 9, 11, 30))
								.skills(singleton(Skill.builder()
										.id("4f09647e-c7d3-4aa6-ab3d-0faff66b951f")
										.name("Spring Boot")
										.build()))
								.build()
				)
		);

		mockMvc.perform(get("/users/1f37fb2a-b4d0-4119-9113-4677beb20ae2/communities").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(JwtAuthenticationFactory.withUser(owner)))
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id", is(equalTo("123"))))
				.andExpect(jsonPath("$[0].title", is(equalTo("Java User Group"))))
				.andExpect(jsonPath("$[0].type", is(equalTo("OPEN"))))
				.andExpect(jsonPath("$[0].description", is(equalTo("Community for Java developers"))))
				.andExpect(jsonPath("$[0].managers[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[0].managers[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[0].skills[0].id", is(equalTo("4f09647e-c7d3-4aa6-ab3d-0faff66b951f"))))
				.andExpect(jsonPath("$[0].skills[0].name", is(equalTo("Spring Boot"))))
				.andExpect(jsonPath("$[1].id", is(equalTo("456"))))
				.andExpect(jsonPath("$[1].title", is(equalTo("Scala User Group"))))
				.andExpect(jsonPath("$[1].type", is(equalTo("OPEN"))))
				.andExpect(jsonPath("$[1].description", is(equalTo("Community for Scala developers"))))
				.andExpect(jsonPath("$[1].managers[0].id", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$[1].managers[0].userName", is(equalTo("tester"))))
				.andExpect(jsonPath("$[1].skills[0].id", is(equalTo("4f09647e-c7d3-4aa6-ab3d-0faff66b951f"))))
				.andExpect(jsonPath("$[1].skills[0].name", is(equalTo("Spring Boot"))));
	}

}
