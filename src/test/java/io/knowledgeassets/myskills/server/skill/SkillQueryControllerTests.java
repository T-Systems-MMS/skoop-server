package io.knowledgeassets.myskills.server.skill;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryController;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SkillQueryController.class)
class SkillQueryControllerTests extends AbstractControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SkillQueryService skillQueryService;

	@Test
	@DisplayName("Responds with the list of skills provided by the internal service")
	void respondsWithListOfSkills() throws Exception {
		given(skillQueryService.getSkills()).willReturn(Stream.of(
				Skill.builder().id("123").name("Angular").description("JavaScript Framework").build(),
				Skill.builder().id("456").name("Spring Boot").description("Java Framework").build()
		));

		mockMvc.perform(get("/skills")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").roles("USER")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(2))))
				.andExpect(jsonPath("$[0].id", is(equalTo("123"))))
				.andExpect(jsonPath("$[0].name", is(equalTo("Angular"))))
				.andExpect(jsonPath("$[0].description", is(equalTo("JavaScript Framework"))))
				.andExpect(jsonPath("$[1].id", is(equalTo("456"))))
				.andExpect(jsonPath("$[1].name", is(equalTo("Spring Boot"))))
				.andExpect(jsonPath("$[1].description", is(equalTo("Java Framework"))));
	}

	@Test
	@DisplayName("Responds with the single skill provided by the internal service")
	void respondsWithRequestedSkill() throws Exception {
		given(skillQueryService.getSkillById("123")).willReturn(Optional.of(
				Skill.builder().id("123").name("Angular").description("JavaScript Framework").build()));

		mockMvc.perform(get("/skills/123")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").roles("USER")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("123"))))
				.andExpect(jsonPath("$.name", is(equalTo("Angular"))))
				.andExpect(jsonPath("$.description", is(equalTo("JavaScript Framework"))));
	}
}
