package io.knowledgeassets.myskills.server.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.knowledgeassets.myskills.server.common.Neo4jSessionFactoryConfiguration;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReportRepository;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReportResponse;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.command.UserSkillPriorityReportCommandController;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.command.UserSkillPriorityReportCommandService;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.query.UserSkillPriorityReportQueryService;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReport;
import io.knowledgeassets.myskills.server.skill.Skill;
import io.knowledgeassets.myskills.server.skill.SkillRepository;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryService;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import io.knowledgeassets.myskills.server.userskill.query.UserSkillQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserSkillPriorityReportCommandServiceTests {

	@Mock
	private UserSkillPriorityReportRepository userSkillPriorityReportRepository;
	@Mock
	private UserSkillQueryService userSkillQueryService;
	@Mock
	private UserSkillPriorityReportQueryService userSkillPriorityReportQueryService;

	UserSkillPriorityReportCommandService userSkillPriorityReportCommandService;

	@BeforeEach
	void setUp() {
		userSkillPriorityReportCommandService = new UserSkillPriorityReportCommandService(
				userSkillPriorityReportRepository, userSkillQueryService, userSkillPriorityReportQueryService);
	}

	@Test
	@DisplayName("Creates a report")
	public void createReport() {
		LocalDateTime now = LocalDateTime.now();

		given(userSkillPriorityReportQueryService.getPrioritizedSkillsToCreateReport())
				.willReturn(StreamSupport.stream((
								List.of(UserSkillPriorityAggregationReportResult.builder()
										.averagePriority(3.5)
										.maximumPriority(4.0)
										.userCount(2)
										.skill(Skill.builder()
												.id("89fdda90-8f17-4f5b-8077-63b0dbebcfa1")
												.name("Maven")
												.build())
										.users(List.of(
												User.builder()
														.id("9a2d807d-9fe1-41a4-a6f3-2851a163523a")
														.userName("tester")
														.coach(true)
														.build()
												, User.builder()
														.id("11c8265e-e582-4700-a3c0-647d70046dc6")
														.userName("tester2")
														.build())
										)
										.build())
						).spliterator(), false)
				);

		given(userSkillQueryService.findByUserIdAndSkillName(ArgumentMatchers.any(String.class), ArgumentMatchers.any(String.class)))
				.willReturn(Optional.of(UserSkill.builder()
						.id("6ga0dc67-f217-41e2-862d-efd372614410")
						.currentLevel(2)
						.desiredLevel(4)
						.priority(4)
						.build())
				);

		given(userSkillPriorityReportRepository.save(ArgumentMatchers.any(UserSkillPriorityReport.class)))
				.willReturn(
						UserSkillPriorityReport.builder()
								.id("8f5634b8-783f-4503-b40f-ca93d8db7e72")
								.date(now)
								.userSkillPriorityAggregationReports(singletonList(
										UserSkillPriorityAggregationReport.builder()
												.id("123")
												.skillName("Neo4j")
												.userCount(2)
												.averagePriority(3D)
												.maximumPriority(4D)
												.userSkillReports(asList(
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
														)
												)
												.build()
										)
								)
								.build());

		UserSkillPriorityReport userSkillPriorityReport = userSkillPriorityReportCommandService.createPriorityReport();
		assertThat(userSkillPriorityReport).isNotNull();
		assertThat(userSkillPriorityReport.getId()).isEqualTo("8f5634b8-783f-4503-b40f-ca93d8db7e72");
		assertThat(userSkillPriorityReport.getDate().truncatedTo(ChronoUnit.SECONDS))
				.isEqualTo(now.truncatedTo(ChronoUnit.SECONDS));
		assertThat(userSkillPriorityReport.getUserSkillPriorityAggregationReports().size()).isEqualTo(1);
		List<UserSkillReport> userSkillReports = userSkillPriorityReport.getUserSkillPriorityAggregationReports().get(0).getUserSkillReports();
		assertThat(userSkillPriorityReport.getUserSkillPriorityAggregationReports().get(0).getSkillDescription()).isNull();
		assertThat(userSkillReports.size()).isEqualTo(2);
		assertThat(userSkillReports.get(1).getSkillName()).isEqualTo("Neo4j");
		assertThat(userSkillReports.get(1).getUserName()).isEqualTo("tester2");
		assertThat(userSkillReports.get(1).getCurrentLevel()).isEqualTo(1);
		assertThat(userSkillReports.get(1).getDesiredLevel()).isEqualTo(3);
		assertThat(userSkillReports.get(1).getPriority()).isEqualTo(4);

	}
}
