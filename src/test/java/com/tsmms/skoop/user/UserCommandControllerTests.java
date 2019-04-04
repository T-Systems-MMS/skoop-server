package com.tsmms.skoop.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.common.JwtAuthenticationFactory;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.user.command.UserCommandController;
import com.tsmms.skoop.user.command.UserCommandService;
import com.tsmms.skoop.exception.enums.Model;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserCommandController.class)
public class UserCommandControllerTests extends AbstractControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserCommandService userCommandService;

	@Autowired
	private ObjectMapper objectMapper;

	// TODO: Complete test data and response assertions.

	@Test
	@DisplayName("Creates and returns the given user")
	void createUser() throws Exception {
		given(userCommandService.createUser("tester1", "firstTester", null, "tester1@gmail.com"))
				.willReturn(User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").coach(false).build());

		UserRequest userRequest = UserRequest.builder().userName("tester1").firstName("firstTester").email("tester1@gmail.com").build();
		// TODO: Use plain request string instead of Jackson object mapper.
		String userRequestAsString = objectMapper.writeValueAsString(userRequest);

		mockMvc.perform(post("/users")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(userRequestAsString)
				.with(user("tester").roles("ADMIN"))
				.with(csrf()))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.userName", is(equalTo("tester1"))))
				.andExpect(jsonPath("$.lastName", is(equalTo(null))))
				.andExpect(jsonPath("$.email", is(equalTo("tester1@gmail.com"))))
				.andExpect(jsonPath("$.coach", is(false)));
	}

	@Test
	@DisplayName("Updates and returns the given user")
	void updateUser() throws Exception {
		UserRequest userRequest = UserRequest.builder().userName("tester1").firstName("firstTester").email("tester1@gmail.com").coach(true)
				.academicDegree("Diplom-Wirtschaftsinformatiker")
				.positionProfile("Software Architect")
				.summary("Tester summary")
				.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
				.specializations(Arrays.asList("IT Consulting", "Software Integration"))
				.certificates(Collections.singletonList("Java Certified Programmer"))
				.languages(Collections.singletonList("Deutsch"))
				.build();
		String userRequestAsString = objectMapper.writeValueAsString(userRequest);

		User owner = User.builder()
				.id("123")
				.userName("tester1")
				.build();

		given(userCommandService.updateUser("123", userRequest))
				.willReturn(User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").coach(true)
						.academicDegree("Diplom-Wirtschaftsinformatiker")
						.positionProfile("Software Architect")
						.summary("Tester summary")
						.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
						.specializations(Arrays.asList("IT Consulting", "Software Integration"))
						.certificates(Collections.singletonList("Java Certified Programmer"))
						.languages(Collections.singletonList("Deutsch"))
						.build());

		mockMvc.perform(put("/users/123")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(userRequestAsString)
				.with(authentication(JwtAuthenticationFactory.withUser(owner)))
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.userName", is(equalTo("tester1"))))
				.andExpect(jsonPath("$.lastName", is(equalTo(null))))
				.andExpect(jsonPath("$.email", is(equalTo("tester1@gmail.com"))))
				.andExpect(jsonPath("$.coach", is(true)))
				.andExpect(jsonPath("$.academicDegree", is("Diplom-Wirtschaftsinformatiker")))
				.andExpect(jsonPath("$.positionProfile", is("Software Architect")))
				.andExpect(jsonPath("$.summary", is("Tester summary")))
				.andExpect(jsonPath("$.industrySectors", is(Arrays.asList("Automotive", "Telecommunication"))))
				.andExpect(jsonPath("$.specializations", is(Arrays.asList("IT Consulting", "Software Integration"))))
				.andExpect(jsonPath("$.certificates", is(Collections.singletonList("Java Certified Programmer"))))
				.andExpect(jsonPath("$.languages", is(Collections.singletonList("Deutsch"))));
	}

	@Test
	@DisplayName("Deletes the given existing user")
	void deleteUser() throws Exception {
		mockMvc.perform(delete("/users/123")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").roles("ADMIN"))
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Responds with status 404 if user to be deleted does not exist")
	public void deleteUser_ThrowsException() throws Exception {
		BDDMockito.willThrow(NoSuchResourceException.builder()
				.model(Model.USER)
				.searchParamsMap(new String[]{"id", "123"})
				.build())
				.given(userCommandService).deleteUser("123");

		MvcResult mvcResult = mockMvc.perform(delete("/users/123")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").roles("ADMIN"))
				.with(csrf()))
				.andExpect(status().isNotFound())
				.andReturn();

		String responseJson = mvcResult.getResponse().getContentAsString();

		Exception exception = objectMapper.readValue(responseJson, Exception.class);
		assertThat(exception.getMessage()).isEqualTo("User was not found for parameters {id=123}");
	}

	@Test
	@DisplayName("Responds with status 403 if non-admin principal attempts to delete user")
	void yieldsForbiddenOnNonAdminDeleteRequest() throws Exception {
		mockMvc.perform(delete("/users/123")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").roles("USER"))
				.with(csrf()))
				.andExpect(status().isForbidden())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andDo(result -> {
					then(userCommandService).shouldHaveZeroInteractions();
				});
	}
}
