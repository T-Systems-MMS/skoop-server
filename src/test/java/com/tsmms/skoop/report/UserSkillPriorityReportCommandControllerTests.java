package com.tsmms.skoop.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsmms.skoop.report.userskill.UserSkillReport;
import com.tsmms.skoop.report.userskillpriority.UserSkillPriorityAggregationReport;
import com.tsmms.skoop.report.userskillpriority.UserSkillPriorityReport;
import com.tsmms.skoop.report.userskillpriority.UserSkillPriorityReportSimpleResponse;
import com.tsmms.skoop.report.userskillpriority.command.UserSkillPriorityReportCommandController;
import com.tsmms.skoop.report.userskillpriority.command.UserSkillPriorityReportCommandService;
import com.tsmms.skoop.common.AbstractControllerTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserSkillPriorityReportCommandController.class)
class UserSkillPriorityReportCommandControllerTests extends AbstractControllerTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private UserSkillPriorityReportCommandService userSkillPriorityReportCommandService;

	@Test
	@DisplayName("Creates a report")
	public void createReport() throws Exception {
		LocalDateTime now = LocalDateTime.now();
		given(userSkillPriorityReportCommandService.createUserSkillPriorityReport()).willReturn(
				UserSkillPriorityReport.builder()
						.id("8f5634b8-783f-4503-b40f-ca93d8db7e72")
						.date(now)
						.aggregationReports(singletonList(
								UserSkillPriorityAggregationReport.builder()
										.id("123")
										.skillName("Neo4j")
										.userCount(2)
										.averagePriority(3D)
										.maximumPriority(4D)
										.userSkillReports(asList(
												UserSkillReport.builder()
														.skillName("Neo4j")
														.skillDescription("Graph database")
														.userName("tester")
														.userFirstName("Toni")
														.userLastName("Tester")
														.currentLevel(2)
														.desiredLevel(4)
														.priority(2)
														.build(),
												UserSkillReport.builder()
														.skillName("Neo4j")
														.skillDescription("Graph database")
														.userName("tester2")
														.userFirstName("Tina")
														.userLastName("Testing")
														.currentLevel(1)
														.desiredLevel(3)
														.priority(4)
														.build()
												)
										)
										.build()
								)
						)
						.build()
		);

		MvcResult mvcResult = mockMvc.perform(post("/reports/skills/priority")
				.accept(MediaType.APPLICATION_JSON)
				.with(user("tester").roles("USER"))
				.with(csrf()))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(equalTo("8f5634b8-783f-4503-b40f-ca93d8db7e72"))))
				.andExpect(content().string(containsString(now.truncatedTo(ChronoUnit.SECONDS).toString())))
				.andExpect(jsonPath("$.skillCount", is(equalTo(1))))
				.andReturn();

		String responseJson = mvcResult.getResponse().getContentAsString();

		UserSkillPriorityReportSimpleResponse userSkillPriorityReportSimpleResponse = objectMapper.readValue(responseJson,
				UserSkillPriorityReportSimpleResponse.class);
		assertThat(userSkillPriorityReportSimpleResponse.getDate().truncatedTo(ChronoUnit.SECONDS))
				.isEqualTo(now.truncatedTo(ChronoUnit.SECONDS));

	}
}
