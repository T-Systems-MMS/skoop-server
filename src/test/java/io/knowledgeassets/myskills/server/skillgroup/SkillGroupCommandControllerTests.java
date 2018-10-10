package io.knowledgeassets.myskills.server.skillgroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.knowledgeassets.myskills.server.common.Neo4jSessionFactoryConfiguration;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.skillgroup.command.SkillGroupCommandController;
import io.knowledgeassets.myskills.server.skillgroup.command.SkillGroupCommandService;
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
@WebMvcTest(SkillGroupCommandController.class)
// Additional configuration is required to workaround missing SessionFactory issue!
@Import(Neo4jSessionFactoryConfiguration.class)
public class SkillGroupCommandControllerTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private SkillGroupCommandService skillGroupCommandService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("Create Skill group")
	void createSkillGroup() throws Exception {
		SkillGroupRequest skillGroupRequest = SkillGroupRequest.builder().name("Programming").description("programming languages group").build();
		String skillRequestAsString = objectMapper.writeValueAsString(skillGroupRequest);

		given(skillGroupCommandService.createGroup("Programming", "programming languages group"))
				.willReturn(SkillGroup.builder().id("123").name("Programming").description("programming languages group").build());

		mockMvc.perform(post("/groups")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(skillRequestAsString)
				.with(csrf())
				.with(user("tester").password("123").roles("USER"))
		)
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.name", is(equalTo("Programming"))))
				.andExpect(jsonPath("$.description", is(equalTo("programming languages group"))));
	}

	@Test
	@DisplayName("Update Skill group")
	void updateSkillGroup() throws Exception {

		given(skillGroupCommandService.updateGroup("123", "Programming", "programming languages group"))
				.willReturn(SkillGroup.builder().id("123").name("Programming").description("programming languages group").build());

		SkillGroupRequest skillGroupRequest = SkillGroupRequest.builder().name("Programming").description("programming languages group").build();
		String skillGroupRequestAsString = objectMapper.writeValueAsString(skillGroupRequest);
		String groupId = "123";
		mockMvc.perform(put("/groups/" + groupId)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(skillGroupRequestAsString)
				.with(csrf())
				.with(user("tester").password("123").roles("USER"))
		)
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.name", is(equalTo("Programming"))))
				.andExpect(jsonPath("$.description", is(equalTo("programming languages group"))));
	}

	@Test
	@DisplayName("Delete Skill group")
	void deleteSkillGroup() throws Exception {

		String skillId = "123";
		mockMvc.perform(delete("/groups/" + skillId)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf())
				.with(user("tester").password("123").roles("USER"))
		)
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Delete Skill group that does not exist")
	public void deleteSkillGroup_ThrowsException() throws Exception {

		doThrow(NoSuchResourceException.builder()
				.model(Model.SKILL_GROUP)
				.searchParamsMap(new String[]{"id", "123"})
				.build()).when(skillGroupCommandService).deleteGroup("123");

		String groupId = "123";
		MvcResult mvcResult = mockMvc.perform(delete("/groups/" + groupId)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.with(csrf())
				.with(user("tester").password("123").roles("USER")))
				.andExpect(status().isNotFound())
				.andReturn();

		String responseJson = mvcResult.getResponse().getContentAsString();

		Exception exception = objectMapper.readValue(responseJson, Exception.class);
		assertThat(exception.getMessage()).isEqualTo("Skill group was not found for parameters {id=123}");
	}

}
