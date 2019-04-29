package com.tsmms.skoop.user.query;

import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;

@WebMvcTest(UserSuggestionController.class)
class UserSuggestionControllerTests extends AbstractControllerTests {

	@MockBean
	private UserQueryService userQueryService;

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("Gets user suggestions.")
	@Test
	void getUserSuggestions() throws Exception {
		final User owner = User.builder()
				.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
				.userName("tester")
				.build();

		given(userQueryService.getUsersBySearchTerm("t")).willReturn(Stream.of(
				User.builder()
						.id("56ef4778-a084-4509-9a3e-80b7895cf7b0")
						.userName("tester")
						.firstName("Toni")
						.lastName("Tester")
						.email("toni.tester@skoop.io")
						.coach(true)
						.academicDegree("Diplom-Wirtschaftsinformatiker")
						.positionProfile("Software Architect")
						.summary("Toni's summary")
						.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
						.specializations(Arrays.asList("Management", "Software Integration"))
						.certificates(Collections.singletonList("Java Certified Programmer"))
						.languages(Collections.singletonList("Deutsch"))
						.build(),
				User.builder()
						.id("d9d74c04-0ab0-479c-a1d7-d372990f11b6")
						.userName("testing")
						.firstName("Tina")
						.lastName("Testing")
						.email("tina.testing@skoop.io")
						.coach(false)
						.academicDegree("Diplom-Wirtschaftsinformatiker")
						.positionProfile("Software Engineer")
						.summary("Tina's summary")
						.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
						.specializations(Arrays.asList("IT Consulting", "Software Integration"))
						.certificates(Collections.singletonList("Kotlin Certified Programmer"))
						.languages(Collections.singletonList("English"))
						.build(),
				User.builder()
						.id("c9d0da43-ac3d-40a3-9864-f4267acaddfc")
						.userName("test")
						.firstName("Tom")
						.lastName("Test")
						.email("tom.test@skoop.io")
						.coach(false)
						.academicDegree("Diplom-Wirtschaftsinformatiker")
						.positionProfile("Software Engineer")
						.summary("Tom's summary")
						.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
						.specializations(Arrays.asList("IT Consulting", "Software Integration"))
						.certificates(Collections.singletonList("Scala Certified Programmer"))
						.languages(Collections.singletonList("Spanish"))
						.build()
		));

		mockMvc.perform(get("/user-suggestions")
				.param("search", "t")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].id", is(equalTo("d9d74c04-0ab0-479c-a1d7-d372990f11b6"))))
				.andExpect(jsonPath("$[0].userName", is(equalTo("testing"))))
				.andExpect(jsonPath("$[0].firstName", is(equalTo("Tina"))))
				.andExpect(jsonPath("$[0].lastName", is(equalTo("Testing"))))
				.andExpect(jsonPath("$[0].email", is(equalTo("tina.testing@skoop.io"))))
				.andExpect(jsonPath("$[0].coach", is(equalTo(false))))
				.andExpect(jsonPath("$[1].id", is(equalTo("c9d0da43-ac3d-40a3-9864-f4267acaddfc"))))
				.andExpect(jsonPath("$[1].userName", is(equalTo("test"))))
				.andExpect(jsonPath("$[1].firstName", is(equalTo("Tom"))))
				.andExpect(jsonPath("$[1].lastName", is(equalTo("Test"))))
				.andExpect(jsonPath("$[1].email", is(equalTo("tom.test@skoop.io"))))
				.andExpect(jsonPath("$[1].coach", is(equalTo(false))));
	}

	@DisplayName("Not authenticated user cannot get user suggestions.")
	@Test
	void notAuthenticatedUserCannotGetUserSuggestions() throws Exception {
		mockMvc.perform(get("/user-suggestions")
				.param("search", "t")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}

}
