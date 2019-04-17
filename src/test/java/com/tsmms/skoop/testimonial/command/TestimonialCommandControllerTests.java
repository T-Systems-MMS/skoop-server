package com.tsmms.skoop.testimonial.command;

import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.skill.query.SkillQueryService;
import com.tsmms.skoop.testimonial.Testimonial;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.BDDMockito.given;

@WebMvcTest(TestimonialCommandController.class)
class TestimonialCommandControllerTests extends AbstractControllerTests {

	@MockBean
	private SkillQueryService skillQueryService;

	@MockBean
	private UserQueryService userQueryService;

	@MockBean
	private TestimonialCommandService testimonialCommandService;

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("Creates testimonial.")
	@Test
	void createTestimonial() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		final ClassPathResource body = new ClassPathResource("testimonials/create-testimonial.json");

		given(userQueryService.getUserById(tester.getId()))
				.willReturn(Optional.of(tester));

		given(skillQueryService.skillNamesToSkills(Arrays.asList("Java", "Spring Boot")))
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

		given(testimonialCommandService.create(Testimonial.builder()
				.author("John Doe. Some company. CEO.")
				.comment("He is the best developer I have ever worked with.")
				.skills(Arrays.asList(
						Skill.builder()
								.id("123")
								.name("Java")
								.build(),
						Skill.builder()
								.name("Spring Boot")
								.build()
				))
				.user(tester)
				.build()
		)).willReturn(
				Testimonial.builder()
						.id("abc")
						.author("John Doe. Some company. CEO.")
						.comment("He is the best developer I have ever worked with.")
						.skills(Arrays.asList(
								Skill.builder()
										.id("123")
										.name("Java")
										.build(),
								Skill.builder()
										.name("Spring Boot")
										.build()
						))
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
						.creationDatetime(LocalDateTime.of(2019, 4, 17, 10, 0))
						.lastModifiedDatetime(LocalDateTime.of(2019, 4, 17, 10, 0))
						.user(tester)
						.build()
		);

		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/testimonials")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(tester)))
					.with(csrf()))
					.andExpect(status().isCreated())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.id", is(equalTo("abc"))))
					.andExpect(jsonPath("$.author", is(equalTo("John Doe. Some company. CEO."))))
					.andExpect(jsonPath("$.comment", is(equalTo("He is the best developer I have ever worked with."))))
					.andExpect(jsonPath("$.creationDatetime", is(equalTo("2019-04-17T10:00:00"))))
					.andExpect(jsonPath("$.lastModifiedDatetime", is(equalTo("2019-04-17T10:00:00"))))
					.andExpect(jsonPath("$.skills[0].id", is(equalTo("123"))))
					.andExpect(jsonPath("$.skills[0].name", is(equalTo("Java"))))
					.andExpect(jsonPath("$.skills[1].id", is(equalTo("456"))))
					.andExpect(jsonPath("$.skills[1].name", is(equalTo("Spring Boot"))));
		}
	}

	@DisplayName("Not authenticated user cannot create testimonials.")
	@Test
	void notAuthenticatedUserCannotCreateTestimonials() throws Exception {
		final ClassPathResource body = new ClassPathResource("testimonials/create-testimonial.json");
		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/testimonials")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(csrf()))
					.andExpect(status().isUnauthorized());
		}
	}

	@DisplayName("User cannot create testimonials on other users.")
	@Test
	void userCannotCreateTestimonialsOnOtherUsers() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		final ClassPathResource body = new ClassPathResource("testimonials/create-testimonial.json");

		try (final InputStream is = body.getInputStream()) {
			mockMvc.perform(post("/users/c9cf7118-5f9e-40fc-9d89-28b2d0a77340/testimonials")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(is.readAllBytes())
					.with(authentication(withUser(tester)))
					.with(csrf()))
					.andExpect(status().isForbidden());
		}
	}

}
