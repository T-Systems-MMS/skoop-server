package io.knowledgeassets.myskills.server.skillgroup;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.skillgroup.query.SkillGroupQueryController;
import io.knowledgeassets.myskills.server.skillgroup.query.SkillGroupQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SkillGroupQueryController.class)
class SkillGroupQueryControllerTests extends AbstractControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SkillGroupQueryService skillGroupQueryService;

	@Test
	@DisplayName("Responds with the list of skill groups provided by the internal service")
	void respondsWithListOfSkillGroups() throws Exception {
		given(skillGroupQueryService.getSkillGroups()).willReturn(Stream.of(
				SkillGroup.builder().id("123").name("Programming").description("programming languages group").build(),
				SkillGroup.builder().id("456").name("Web").description("web Framework group").build()
		));

		mockMvc.perform(get("/groups")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").roles("USER")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].id", is(equalTo("123"))))
				.andExpect(jsonPath("$[0].name", is(equalTo("Programming"))))
				.andExpect(jsonPath("$[0].description", is(equalTo("programming languages group"))))
				.andExpect(jsonPath("$[1].id", is(equalTo("456"))))
				.andExpect(jsonPath("$[1].name", is(equalTo("Web"))))
				.andExpect(jsonPath("$[1].description", is(equalTo("web Framework group"))));
	}

	@Test
	@DisplayName("Responds with the single skill group provided by the internal service")
	void respondsWithRequestedSkillGroup() throws Exception {
		given(skillGroupQueryService.getSkillGroupById("123")).willReturn(Optional.of(
				SkillGroup.builder().id("123").name("Programming").description("programming languages group").build()));

		mockMvc.perform(get("/groups/123")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").roles("USER")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.name", is(equalTo("Programming"))))
				.andExpect(jsonPath("$.description", is(equalTo("programming languages group"))));
	}
}
