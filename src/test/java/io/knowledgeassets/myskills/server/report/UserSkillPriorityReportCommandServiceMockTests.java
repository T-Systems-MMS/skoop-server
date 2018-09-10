package io.knowledgeassets.myskills.server.report;

import io.knowledgeassets.myskills.server.common.Neo4jSessionFactoryConfiguration;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.query.UserSkillPriorityAggregationReportQueryService;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReportRepository;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.command.UserSkillPriorityReportCommandController;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.command.UserSkillPriorityReportCommandService;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.query.UserSkillPriorityReportQueryService;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReport;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillRepository;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryController;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryService;
import io.knowledgeassets.myskills.server.user.UserRepository;
import io.knowledgeassets.myskills.server.userskill.UserSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserSkillPriorityReportCommandController.class)
// Additional configuration is required to workaround missing SessionFactory issue!
@Import(Neo4jSessionFactoryConfiguration.class)
public class UserSkillPriorityReportCommandServiceMockTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserSkillPriorityReportCommandService userSkillPriorityReportCommandService;

//	@Autowired
//	private WebApplicationContext webApplicationContext;


	@BeforeEach
	public void init() throws Exception {
//		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
	}

	@Test
//	@WithMockUser(username="tester",roles={"USER","ADMIN"})
	public void createReport() throws Exception {
		given(userSkillPriorityReportCommandService.createPriorityReport()).willReturn(
				UserSkillPriorityReport.builder()
						.id("1")
						.date(LocalDateTime.now())
						.userSkillPriorityAggregationReports(
								Stream.of(
										UserSkillPriorityAggregationReport.builder()
												.id("123")
												.skillName("Neo4j")
												.userCount(2)
												.averagePriority(3D)
												.maximumPriority(4D)
												.userSkillReports(
														Stream.of(
																UserSkillReport.builder()
																		.skillName("Neo4j")
																		.userName("tester")
																		.currentLevel(2)
																		.desiredLevel(4)
																		.priority(2)
																		.build(),
																UserSkillReport.builder()
																		.skillName("Neo4j")
																		.userName("tester2")
																		.currentLevel(1)
																		.desiredLevel(3)
																		.priority(4)
																		.build()
														).collect(Collectors.toList())
												)
												.build()
								).collect(Collectors.toList())
						)
						.build()
		);
		mockMvc.perform(post("/reports").accept(MediaType.APPLICATION_JSON)
				.with(user("tester").password("123").roles("USER")))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(equalTo(1))))
				.andExpect(jsonPath("$[0].id", is(equalTo("123"))))
				.andExpect(jsonPath("$[0].skillName", is(equalTo("Neo4j"))))
				.andExpect(jsonPath("$[0].userCount", is(equalTo(2))));
	}
}
