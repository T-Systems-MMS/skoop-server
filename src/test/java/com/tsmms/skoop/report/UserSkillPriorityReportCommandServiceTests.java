package com.tsmms.skoop.report;

import com.tsmms.skoop.report.userskill.UserSkillReport;
import com.tsmms.skoop.report.userskillpriority.UserSkillPriorityAggregationReport;
import com.tsmms.skoop.report.userskillpriority.UserSkillPriorityAggregationReportResult;
import com.tsmms.skoop.report.userskillpriority.UserSkillPriorityReport;
import com.tsmms.skoop.report.userskillpriority.UserSkillPriorityReportRepository;
import com.tsmms.skoop.report.userskillpriority.command.UserSkillPriorityReportCommandService;
import com.tsmms.skoop.report.userskillpriority.query.UserSkillPriorityReportQueryService;
import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.userskill.UserSkill;
import com.tsmms.skoop.userskill.query.UserSkillQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserSkillPriorityReportCommandServiceTests {
	@Mock
	private UserSkillPriorityReportRepository userSkillPriorityReportRepository;
	@Mock
	private UserSkillQueryService userSkillQueryService;
	@Mock
	private UserSkillPriorityReportQueryService userSkillPriorityReportQueryService;

	private UserSkillPriorityReportCommandService userSkillPriorityReportCommandService;

	@BeforeEach
	void setUp() {
		userSkillPriorityReportCommandService = new UserSkillPriorityReportCommandService(
				userSkillPriorityReportRepository, userSkillPriorityReportQueryService, userSkillQueryService);
	}

	@Test
	@DisplayName("Creates a user skill priority report")
	void createReport() {
		LocalDateTime now = LocalDateTime.now();

		// TODO: Make given test data more consistent.

		given(userSkillPriorityReportQueryService.getAllUserSkillPriorityAggregationResults())
				.willReturn(Stream.of(UserSkillPriorityAggregationReportResult.builder()
						.averagePriority(3.5)
						.maximumPriority(4.0)
						.userCount(2)
						.skill(Skill.builder()
								.id("89fdda90-8f17-4f5b-8077-63b0dbebcfa1")
								.name("Maven")
								.description("Build tool")
								.build())
						.users(List.of(
								User.builder()
										.id("9a2d807d-9fe1-41a4-a6f3-2851a163523a")
										.userName("tester")
										.firstName("Toni")
										.lastName("Tester")
										.build()
								, User.builder()
										.id("11c8265e-e582-4700-a3c0-647d70046dc6")
										.userName("tester2")
										.firstName("Tina")
										.lastName("Testing")
										.build())
						)
						.build())
				);

		given(userSkillQueryService.getUserSkillByUserIdAndSkillId(any(String.class), any(String.class)))
				.willReturn(Optional.of(UserSkill.builder()
						.id(1L)
						.currentLevel(2)
						.desiredLevel(4)
						.priority(4)
						.build())
				);

		// TODO: Verify that repository is really called with a report built from data returned from other services.

		given(userSkillPriorityReportRepository.save(any(UserSkillPriorityReport.class)))
				.willReturn(UserSkillPriorityReport.builder()
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
						.build());

		UserSkillPriorityReport userSkillPriorityReport = userSkillPriorityReportCommandService.createUserSkillPriorityReport();
		assertThat(userSkillPriorityReport).isNotNull();
		assertThat(userSkillPriorityReport.getId()).isEqualTo("8f5634b8-783f-4503-b40f-ca93d8db7e72");
		assertThat(userSkillPriorityReport.getDate().truncatedTo(ChronoUnit.SECONDS))
				.isEqualTo(now.truncatedTo(ChronoUnit.SECONDS));
		assertThat(userSkillPriorityReport.getAggregationReports().size()).isEqualTo(1);
		List<UserSkillReport> userSkillReports = userSkillPriorityReport.getAggregationReports().get(0).getUserSkillReports();
		assertThat(userSkillPriorityReport.getAggregationReports().get(0).getSkillDescription()).isNull();
		assertThat(userSkillReports.size()).isEqualTo(2);
		assertThat(userSkillReports.get(1).getSkillName()).isEqualTo("Neo4j");
		assertThat(userSkillReports.get(1).getUserName()).isEqualTo("tester2");
		assertThat(userSkillReports.get(1).getCurrentLevel()).isEqualTo(1);
		assertThat(userSkillReports.get(1).getDesiredLevel()).isEqualTo(3);
		assertThat(userSkillReports.get(1).getPriority()).isEqualTo(4);
	}
}
