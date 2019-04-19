package com.tsmms.skoop.publication.command;

import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.publication.Publication;
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
import java.time.LocalDate;
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

@WebMvcTest(PublicationCommandController.class)
class PublicationCommandControllerTests extends AbstractControllerTests {

	@MockBean
	private UserQueryService userQueryService;

	@MockBean
	private SkillQueryService skillQueryService;

	@MockBean
	private PublicationCommandService publicationCommandService;

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("Creates publication.")
	@Test
	void createPublication() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		final ClassPathResource body = new ClassPathResource("publications/create-publication.json");

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

		given(publicationCommandService.create(Publication.builder()
				.title("The first publication")
				.publisher("The first publisher")
				.date(LocalDate.of(2019, 4, 19))
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
				.willReturn(Publication.builder()
						.id("123")
						.title("The first publication")
						.publisher("The first publisher")
						.date(LocalDate.of(2019, 4, 19))
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
			mockMvc.perform(post("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/publications")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(tester)))
					.with(csrf()))
					.andExpect(status().isCreated())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.id", is(equalTo("123"))))
					.andExpect(jsonPath("$.title", is(equalTo("The first publication"))))
					.andExpect(jsonPath("$.publisher", is(equalTo("The first publisher"))))
					.andExpect(jsonPath("$.creationDatetime", is(equalTo("2019-04-19T13:00:00"))))
					.andExpect(jsonPath("$.lastModifiedDatetime", is(equalTo("2019-04-19T13:00:00"))))
					.andExpect(jsonPath("$.link", is(equalTo("http://first-link.com"))))
					.andExpect(jsonPath("$.date", is(equalTo("2019-04-19"))))
					.andExpect(jsonPath("$.skills[0].id", is(equalTo("123"))))
					.andExpect(jsonPath("$.skills[0].name", is(equalTo("Java"))))
					.andExpect(jsonPath("$.skills[1].id", is(equalTo("456"))))
					.andExpect(jsonPath("$.skills[1].name", is(equalTo("Spring Boot"))));
		}
	}

	@DisplayName("Not authenticated user cannot create publications.")
	@Test
	void notAuthenticatedUserCannotCreatePublications() throws Exception {
		final ClassPathResource body = new ClassPathResource("publications/create-publication.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/publications")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(csrf()))
					.andExpect(status().isUnauthorized());
		}
	}

	@DisplayName("User cannot create publications of other users.")
	@Test
	void userCannotCreatePublicattionsOfOtherUsers() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		final ClassPathResource body = new ClassPathResource("publications/create-publication.json");

		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/users/c9cf7118-5f9e-40fc-9d89-28b2d0a77340/publications")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(tester)))
					.with(csrf()))
					.andExpect(status().isForbidden());
		}
	}

	@DisplayName("Updates publication.")
	@Test
	void updatePublication() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		final ClassPathResource body = new ClassPathResource("publications/update-publication.json");

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

		given(publicationCommandService.update("123", PublicationUpdateCommand.builder()
				.publisher("The first publisher updated")
				.link("http://first-updated-link.com")
				.title("The first publication updated")
				.date(LocalDate.of(2020, 4, 19))
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
				Publication.builder()
						.id("123")
						.title("The first publication updated")
						.publisher("The first publisher updated")
						.date(LocalDate.of(2020, 4, 19))
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
			mockMvc.perform(put("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/publications/123")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(tester)))
					.with(csrf()))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.id", is(equalTo("123"))))
					.andExpect(jsonPath("$.title", is(equalTo("The first publication updated"))))
					.andExpect(jsonPath("$.publisher", is(equalTo("The first publisher updated"))))
					.andExpect(jsonPath("$.creationDatetime", is(equalTo("2019-04-22T13:00:00"))))
					.andExpect(jsonPath("$.lastModifiedDatetime", is(equalTo("2019-04-22T13:00:00"))))
					.andExpect(jsonPath("$.link", is(equalTo("http://first-updated-link.com"))))
					.andExpect(jsonPath("$.date", is(equalTo("2020-04-19"))))
					.andExpect(jsonPath("$.skills[0].id", is(equalTo("123"))))
					.andExpect(jsonPath("$.skills[0].name", is(equalTo("Java"))))
					.andExpect(jsonPath("$.skills[1].id", is(equalTo("456"))))
					.andExpect(jsonPath("$.skills[1].name", is(equalTo("Spring Boot"))))
					.andExpect(jsonPath("$.skills[2].id", is(equalTo("789"))))
					.andExpect(jsonPath("$.skills[2].name", is(equalTo("Angular"))));
		}
	}

	@DisplayName("Not authenticated user cannot update publications.")
	@Test
	void notAuthenticatedUserCannotUpdatePublications() throws Exception {
		final ClassPathResource body = new ClassPathResource("publications/update-publication.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/publications/123")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(csrf()))
					.andExpect(status().isUnauthorized());
		}
	}

	@DisplayName("User cannot update publications of other users.")
	@Test
	void userCannotUpdatePublicationsOfOtherUsers() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		final ClassPathResource body = new ClassPathResource("publications/update-publication.json");

		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(put("/users/c9cf7118-5f9e-40fc-9d89-28b2d0a77340/publications/123")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(tester)))
					.with(csrf()))
					.andExpect(status().isForbidden());
		}
	}

	@DisplayName("Deletes publication.")
	@Test
	void deletesPublication() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();
		mockMvc.perform(delete("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/publications/123")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(tester)))
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@DisplayName("Not authenticated user cannot delete publications.")
	@Test
	void notAuthenticatedUserCannotDeletePublications() throws Exception {
		mockMvc.perform(delete("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/publications/123")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

	@DisplayName("User cannot delete publications of other users.")
	@Test
	void userCannotDeletePublicationsOfOtherUsers() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();
		mockMvc.perform(delete("/users/c9cf7118-5f9e-40fc-9d89-28b2d0a77340/publications/123")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(tester)))
				.with(csrf()))
				.andExpect(status().isForbidden());
	}


}
