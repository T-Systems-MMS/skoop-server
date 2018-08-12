package io.knowledgeassets.myskills.server.userskill.query;

import io.knowledgeassets.myskills.server.common.Neo4jSessionFactoryConfiguration;
import io.knowledgeassets.myskills.server.skill.Skill;
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

import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserSkillSuggestionController.class)
// Additional configuration is required to workaround missing SessionFactory issue!
@Import(Neo4jSessionFactoryConfiguration.class)
class UserSkillSuggestionControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserSkillQueryService userSkillQueryService;

	@Test
	@DisplayName("Responds with the list of skill names provided by the internal service")
	void providesSkillSuggestions() throws Exception {
		given(userSkillQueryService.getUserSkillSuggestions("123", "spr"))
				.willReturn(Stream.of(
						Skill.builder().id("456").name("Spring Boot").description("Java Application Framework").build(),
						Skill.builder().id("789").name("Spring Data").description("Java Data Access Framework").build()
				));
		mockMvc.perform(get("/users/123/skill-suggestions").param("search", "spr")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").password("123").roles("USER")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0]", is(equalTo("Spring Boot"))))
				.andExpect(jsonPath("$[1]", is(equalTo("Spring Data"))));
	}
}
