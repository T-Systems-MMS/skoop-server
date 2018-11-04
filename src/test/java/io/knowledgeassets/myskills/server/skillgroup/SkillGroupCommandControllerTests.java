package io.knowledgeassets.myskills.server.skillgroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SkillGroupCommandController.class)
public class SkillGroupCommandControllerTests extends AbstractControllerTests {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private SkillGroupCommandService skillGroupCommandService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("Creates and returns the given skill group")
	void createSkillGroup() throws Exception {
		SkillGroupRequest skillGroupRequest = SkillGroupRequest.builder().name("Programming").description("programming languages group").build();
		// TODO: Use plain request string instead of Jackson object mapper.
		String skillRequestAsString = objectMapper.writeValueAsString(skillGroupRequest);

		given(skillGroupCommandService.createGroup("Programming", "programming languages group"))
				.willReturn(SkillGroup.builder().id("123").name("Programming").description("programming languages group").build());

		mockMvc.perform(post("/groups")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(skillRequestAsString)
				.with(user("tester").roles("USER"))
				.with(csrf()))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.name", is(equalTo("Programming"))))
				.andExpect(jsonPath("$.description", is(equalTo("programming languages group"))));
	}

	@Test
	@DisplayName("Updates and returns the given skill group")
	void updateSkillGroup() throws Exception {
		given(skillGroupCommandService.updateGroup("123", "Programming", "programming languages group"))
				.willReturn(SkillGroup.builder().id("123").name("Programming").description("programming languages group").build());

		SkillGroupRequest skillGroupRequest = SkillGroupRequest.builder().name("Programming").description("programming languages group").build();
		String skillGroupRequestAsString = objectMapper.writeValueAsString(skillGroupRequest);

		mockMvc.perform(put("/groups/123")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(skillGroupRequestAsString)
				.with(user("tester").roles("USER"))
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.name", is(equalTo("Programming"))))
				.andExpect(jsonPath("$.description", is(equalTo("programming languages group"))));
	}

	@Test
	@DisplayName("Deletes the given existing skill group")
	void deleteSkillGroup() throws Exception {
		mockMvc.perform(delete("/groups/123")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").roles("ADMIN"))
				.with(csrf()))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Responds with status 404 if skill group to be deleted does not exist")
	public void deleteSkillGroup_ThrowsException() throws Exception {
		willThrow(NoSuchResourceException.builder()
				.model(Model.SKILL_GROUP)
				.searchParamsMap(new String[]{"id", "123"})
				.build())
				.given(skillGroupCommandService).deleteGroup("123");

		MvcResult mvcResult = mockMvc.perform(delete("/groups/123")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").roles("ADMIN"))
				.with(csrf()))
				.andExpect(status().isNotFound())
				.andReturn();

		String responseJson = mvcResult.getResponse().getContentAsString();

		Exception exception = objectMapper.readValue(responseJson, Exception.class);
		assertThat(exception.getMessage()).isEqualTo("Skill group was not found for parameters {id=123}");
	}
}
