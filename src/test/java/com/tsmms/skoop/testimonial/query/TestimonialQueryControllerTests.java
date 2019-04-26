package com.tsmms.skoop.testimonial.query;

import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.testimonial.Testimonial;
import com.tsmms.skoop.user.User;
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

import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestimonialQueryController.class)
class TestimonialQueryControllerTests extends AbstractControllerTests {

	@MockBean
	private TestimonialQueryService testimonialQueryService;

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("Gets user testimonials.")
	@Test
	void getsUserTestimonials() throws Exception {

		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		given(testimonialQueryService.getUserTestimonials("56ef4778-a084-4509-9a3e-80b7895cf7b0"))
				.willReturn(Stream.of(
						Testimonial.builder()
								.id("abc")
								.author("John Doe. Some company. CEO.")
								.comment("He is the best developer I have ever worked with.")
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
								.creationDatetime(LocalDateTime.of(2019, 4, 17, 10, 0))
								.lastModifiedDatetime(LocalDateTime.of(2019, 4, 17, 10, 0))
								.user(tester)
								.build(),
						Testimonial.builder()
								.id("def")
								.author("Jenny Doe. Another company. CEO.")
								.comment("He is one of the best developers I have ever worked with.")
								.skills(new HashSet<>(Arrays.asList(
										Skill.builder()
												.id("789")
												.name("JavaScript")
												.build(),
										Skill.builder()
												.id("456")
												.name("Spring Boot")
												.build()
								)))
								.creationDatetime(LocalDateTime.of(2018, 4, 17, 10, 0))
								.lastModifiedDatetime(LocalDateTime.of(2018, 4, 17, 10, 0))
								.user(tester)
								.build()
				));

		mockMvc.perform(get("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/testimonials")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(tester)))
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id", is(equalTo("abc"))))
				.andExpect(jsonPath("$[0].author", is(equalTo("John Doe. Some company. CEO."))))
				.andExpect(jsonPath("$[0].comment", is(equalTo("He is the best developer I have ever worked with."))))
				.andExpect(jsonPath("$[0].creationDatetime", is(equalTo("2019-04-17T10:00:00"))))
				.andExpect(jsonPath("$[0].lastModifiedDatetime", is(equalTo("2019-04-17T10:00:00"))))
				.andExpect(jsonPath("$[0].skills[?(@.id=='123')].name", hasItem("Java")))
				.andExpect(jsonPath("$[0].skills[?(@.id=='456')].name", hasItem("Spring Boot")))
				.andExpect(jsonPath("$[1].id", is(equalTo("def"))))
				.andExpect(jsonPath("$[1].author", is(equalTo("Jenny Doe. Another company. CEO."))))
				.andExpect(jsonPath("$[1].comment", is(equalTo("He is one of the best developers I have ever worked with."))))
				.andExpect(jsonPath("$[1].creationDatetime", is(equalTo("2018-04-17T10:00:00"))))
				.andExpect(jsonPath("$[1].lastModifiedDatetime", is(equalTo("2018-04-17T10:00:00"))))
				.andExpect(jsonPath("$[1].skills[?(@.id=='789')].name", hasItem("JavaScript")))
				.andExpect(jsonPath("$[1].skills[?(@.id=='456')].name", hasItem("Spring Boot")));
	}

	@DisplayName("Not authenticated user cannot get user testimonials.")
	@Test
	void notAuthenticatedUserCannotGetUserTestimonials() throws Exception {
		mockMvc.perform(get("/users/56ef4778-a084-4509-9a3e-80b7895cf7b0/testimonials")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().isUnauthorized());
	}

	@DisplayName("User cannot get testimonials on other users.")
	@Test
	void userCannotGetTestimonialsOnOtherUsers() throws Exception {
		final User tester = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();
		mockMvc.perform(get("/users/c9cf7118-5f9e-40fc-9d89-28b2d0a77340/testimonials")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(tester)))
				.with(csrf()))
				.andExpect(status().isForbidden());
	}

}
