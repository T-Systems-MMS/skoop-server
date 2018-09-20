package io.knowledgeassets.myskills.server.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.knowledgeassets.myskills.server.common.Neo4jSessionFactoryConfiguration;
import io.knowledgeassets.myskills.server.user.command.UserCommandController;
import io.knowledgeassets.myskills.server.user.command.UserCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserCommandController.class)
// Additional configuration is required to workaround missing SessionFactory issue!
@Import(Neo4jSessionFactoryConfiguration.class)
public class UserCommandControllerTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserCommandService userCommandService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("Create User")
	void createUser() throws Exception {
		given(userCommandService.createUser("tester1", "firstTester", null, "tester1@gmail.com"))
				.willReturn(User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").coach(false).build());

		UserRequest userRequest = UserRequest.builder().userName("tester1").firstName("firstTester").email("tester1@gmail.com").build();
		String userRequestAsString = objectMapper.writeValueAsString(userRequest);

		mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(userRequestAsString)
				.with(csrf())
				.with(user("tester").password("123").roles("USER"))
		)
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.userName", is(equalTo("tester1"))))
				.andExpect(jsonPath("$.lastName", is(equalTo(null))))
				.andExpect(jsonPath("$.email", is(equalTo("tester1@gmail.com"))))
				.andExpect(jsonPath("$.coach", is(false)));
	}

	@Test
	@DisplayName("Update User")
	void updateUser() throws Exception {

		UserRequest userRequest = UserRequest.builder().userName("tester1").firstName("firstTester").email("tester1@gmail.com").coach(true).build();
		String userRequestAsString = objectMapper.writeValueAsString(userRequest);

		given(userCommandService.updateUser("123", userRequest))
				.willReturn(User.builder().id("123").userName("tester1").firstName("firstTester").email("tester1@gmail.com").coach(true).build());

		String userId = "123";
		mockMvc.perform(put("/users/" + userId )
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(userRequestAsString)
				.with(csrf())
				.with(user("tester").password("123").roles("USER"))
		)
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.userName", is(equalTo("tester1"))))
				.andExpect(jsonPath("$.lastName", is(equalTo(null))))
				.andExpect(jsonPath("$.email", is(equalTo("tester1@gmail.com"))))
				.andExpect(jsonPath("$.coach", is(true)));
	}

	@Test
	@DisplayName("Delete User")
	void deleteUser() throws Exception {

		String userId = "123";
		mockMvc.perform(delete("/users/" + userId)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf())
				.with(user("tester").password("123").roles("USER"))
		)
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Delete User that does not exist")
	public void deleteUser_ThrowsException() throws Exception {

		doThrow(new IllegalArgumentException("User with ID 123 not found")).when(userCommandService).deleteUser("123");

		String userId = "123";
		MvcResult mvcResult = mockMvc.perform(delete("/users/" + userId)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf())
				.with(user("tester").password("123").roles("USER")))
				.andExpect(status().isInternalServerError())
				.andReturn();

		String responseJson = mvcResult.getResponse().getContentAsString();

		IllegalArgumentException exception = objectMapper.readValue(responseJson, IllegalArgumentException.class);
		assertThat(exception.getMessage()).isEqualTo("User with ID 123 not found");
	}

}
