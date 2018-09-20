package io.knowledgeassets.myskills.server.skill;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.knowledgeassets.myskills.server.common.Neo4jSessionFactoryConfiguration;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillRequest;
import io.knowledgeassets.myskills.server.skill.command.SkillCommandController;
import io.knowledgeassets.myskills.server.skill.command.SkillCommandService;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SkillCommandController.class)
// Additional configuration is required to workaround missing SessionFactory issue!
@Import(Neo4jSessionFactoryConfiguration.class)
public class SkillCommandControllerTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private SkillCommandService skillCommandService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("Create Skill")
	void createSkill() throws Exception {
		SkillRequest skillRequest = SkillRequest.builder().name("Java").description("A Programming language").build();
		String skillRequestAsString = objectMapper.writeValueAsString(skillRequest);

		given(skillCommandService.createSkill("Java", "A Programming language"))
				.willReturn(Skill.builder().id("123").name("Java").description("A Programming language").build());

		mockMvc.perform(post("/skills")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(skillRequestAsString)
				.with(csrf())
				.with(user("tester").password("123").roles("USER"))
		)
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.name", is(equalTo("Java"))))
				.andExpect(jsonPath("$.description", is(equalTo("A Programming language"))));
	}

	@Test
	@DisplayName("Update Skill")
	void updateSkill() throws Exception {

		given(skillCommandService.updateSkill("123", "Java", "A Programming language"))
				.willReturn(Skill.builder().id("123").name("Java").description("A Programming language").build());

		SkillRequest skillRequest = SkillRequest.builder().name("Java").description("A Programming language").build();
		String skillRequestAsString = objectMapper.writeValueAsString(skillRequest);
		String skillId = "123";
		mockMvc.perform(put("/skills/" + skillId)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(skillRequestAsString)
				.with(csrf())
				.with(user("tester").password("123").roles("USER"))
		)
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.name", is(equalTo("Java"))))
				.andExpect(jsonPath("$.description", is(equalTo("A Programming language"))));
	}

	@Test
	@DisplayName("Delete Skill")
	void deleteSkill() throws Exception {

		String skillId = "123";
		mockMvc.perform(delete("/skills/" + skillId)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf())
				.with(user("tester").password("123").roles("USER"))
		)
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Delete Skill that does not exist")
	public void deleteSkill_ThrowsException() throws Exception {

		doThrow(new IllegalArgumentException("Skill with ID 123 not found")).when(skillCommandService).deleteSkill("123");

		String skillId = "123";
		MvcResult mvcResult = mockMvc.perform(delete("/skills/" + skillId)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf())
				.with(user("tester").password("123").roles("USER")))
				.andExpect(status().isInternalServerError())
				.andReturn();

		String responseJson = mvcResult.getResponse().getContentAsString();

		IllegalArgumentException exception = objectMapper.readValue(responseJson, IllegalArgumentException.class);
		assertThat(exception.getMessage()).isEqualTo("Skill with ID 123 not found");
	}

}