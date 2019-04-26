package com.tsmms.skoop.publication.query;

import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.publication.Publication;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Stream;

import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.BDDMockito.given;

@WebMvcTest(PublicationQueryController.class)
class PublicationQueryControllerTests extends AbstractControllerTests {

	@MockBean
	private PublicationQueryService publicationQueryService;

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("Gets user publications.")
	@Test
	void getUserPublications() throws Exception {

		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		given(publicationQueryService.getUserPublications("56ef4778-a084-4509-9a3e-80b7895cf7b0"))
				.willReturn(Stream.of(
						Publication.builder()
								.id("123")
								.title("The first publication")
								.publisher("The first publisher")
								.date(LocalDate.of(2019, 4, 19))
								.link("http://first-link.com")
								.skills(new HashSet<>(Arrays.asList(
										Skill.builder()
												.id("123")
												.name("Java")
												.build(),
										Skill.builder()
												.id("456")
												.name("Spring Boot")
												.build()
								)))
								.creationDatetime(LocalDateTime.of(2019, 4, 19, 13, 0))
								.lastModifiedDatetime(LocalDateTime.of(2019, 4, 19, 13, 0))
								.user(tester)
								.build(),
						Publication.builder()
								.id("456")
								.title("The second publication")
								.publisher("The second publisher")
								.date(LocalDate.of(2019, 4, 20))
								.link("http://second-link.com")
								.skills(new HashSet<>(Collections.singletonList(
										Skill.builder()
												.id("123")
												.name("Java")
												.build()
								)))
								.creationDatetime(LocalDateTime.of(2019, 4, 20, 13, 0))
								.lastModifiedDatetime(LocalDateTime.of(2019, 4, 20, 13, 0))
								.user(tester)
								.build()
				));

		mockMvc.perform(get("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/publications")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(tester)))
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id", is(equalTo("123"))))
				.andExpect(jsonPath("$[0].title", is(equalTo("The first publication"))))
				.andExpect(jsonPath("$[0].publisher", is(equalTo("The first publisher"))))
				.andExpect(jsonPath("$[0].link", is(equalTo("http://first-link.com"))))
				.andExpect(jsonPath("$[0].date", is(equalTo("2019-04-19"))))
				.andExpect(jsonPath("$[0].creationDatetime", is(equalTo("2019-04-19T13:00:00"))))
				.andExpect(jsonPath("$[0].lastModifiedDatetime", is(equalTo("2019-04-19T13:00:00"))))
				.andExpect(jsonPath("$[0].skills[?(@.id=='123')].name", hasItem("Java")))
				.andExpect(jsonPath("$[0].skills[?(@.id=='456')].name", hasItem("Spring Boot")))
				.andExpect(jsonPath("$[1].id", is(equalTo("456"))))
				.andExpect(jsonPath("$[1].title", is(equalTo("The second publication"))))
				.andExpect(jsonPath("$[1].publisher", is(equalTo("The second publisher"))))
				.andExpect(jsonPath("$[1].link", is(equalTo("http://second-link.com"))))
				.andExpect(jsonPath("$[1].date", is(equalTo("2019-04-20"))))
				.andExpect(jsonPath("$[1].creationDatetime", is(equalTo("2019-04-20T13:00:00"))))
				.andExpect(jsonPath("$[1].lastModifiedDatetime", is(equalTo("2019-04-20T13:00:00"))))
				.andExpect(jsonPath("$[1].skills[?(@.id=='123')].name", hasItem("Java")));
	}

	@DisplayName("Not authenticated user cannot get user publications.")
	@Test
	void notAuthenticatedUserCannotGetUserPublications() throws Exception {
		mockMvc.perform(get("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/publications")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

	@DisplayName("User cannot get publications of other users.")
	@Test
	void userCannotGetPublicationsOfOtherUsers() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();
		mockMvc.perform(get("/users/c9cf7118-5f9e-40fc-9d89-28b2d0a77340/publications")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(tester)))
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

}
