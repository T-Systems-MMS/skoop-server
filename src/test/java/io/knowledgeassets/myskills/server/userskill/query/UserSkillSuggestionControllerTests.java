package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static io.knowledgeassets.myskills.server.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserSkillSuggestionController.class)
class UserSkillSuggestionControllerTests extends AbstractControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserSkillQueryService userSkillQueryService;

	@Test
	@DisplayName("Responds with the list of skill names")
	void providesSkillSuggestions() throws Exception {
		User owner = User.builder()
				.id("f5f7e332-04d9-4b2a-8fe0-d8c49d14f1d6")
				.userName("tester")
				.build();

		given(userSkillQueryService.getUserSkillSuggestions("f5f7e332-04d9-4b2a-8fe0-d8c49d14f1d6", "spr"))
				.willReturn(Stream.of(
						Skill.builder()
								.id("431abf37-3bbe-40ec-859a-a6fe5450cbab")
								.name("Spring Boot")
								.description("Java Application Framework")
								.build(),
						Skill.builder()
								.id("4b503cfe-36ce-4ce1-b041-ea492f15069e")
								.name("Spring Data")
								.description("Java Data Access Framework")
								.build()
				));

		mockMvc.perform(get("/users/f5f7e332-04d9-4b2a-8fe0-d8c49d14f1d6/skill-suggestions")
				.param("search", "spr")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(owner))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0]", is(equalTo("Spring Boot"))))
				.andExpect(jsonPath("$[1]", is(equalTo("Spring Data"))));
	}

	@Test
	@DisplayName("Responds with status 403 if foreign user attempts to query skill suggestions")
	void yieldsForbiddenOnForeignUserAuthentication() throws Exception {
		User foreigner = User.builder()
				.id("db87d46a-e4ca-451a-903b-e8533e0b924b")
				.userName("testing")
				.build();

		mockMvc.perform(get("/users/f5f7e332-04d9-4b2a-8fe0-d8c49d14f1d6/skill-suggestions")
				.param("search", "spr")
				.accept(MediaType.APPLICATION_JSON)
				.with(authentication(withUser(foreigner))))
				.andExpect(status().isForbidden())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andDo(result -> {
					then(userSkillQueryService).shouldHaveZeroInteractions();
				});
	}
}
