package com.tsmms.skoop.security;

import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.query.UserQueryService;
import com.tsmms.skoop.common.AbstractControllerTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MyIdentityController.class)
class MyIdentityControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserQueryService userQueryService;

	@Test
	@DisplayName("Gets user identity of the authenticated user")
	void getAuthenticatedUserIdentity() throws Exception {

		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("johndoe")
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@mail.com")
				.build();

		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))
				.willReturn(Optional.of(owner));

		mockMvc.perform(get("/my-identity")
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.userId", is(equalTo("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))))
				.andExpect(jsonPath("$.userName", is(equalTo("johndoe"))))
				.andExpect(jsonPath("$.firstName", is(equalTo("John"))))
				.andExpect(jsonPath("$.lastName", is(equalTo("Doe"))))
				.andExpect(jsonPath("$.email", is(equalTo("john.doe@mail.com"))));
	}

	@Test
	@DisplayName("Get user identity of the authenticated user which is not in the database")
	void getUserIdentityOfAuthenticatedUserWhichIsNotInDatabase() throws Exception {

		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("johndoe")
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@mail.com")
				.build();

		given(userQueryService.getUserById("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))
				.willReturn(Optional.empty());

		mockMvc.perform(get("/my-identity")
				.with(authentication(withUser(owner))))
				.andExpect(status().isNotFound());
	}

}
