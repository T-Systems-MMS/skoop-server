package io.knowledgeassets.myskills.server.report;

import io.knowledgeassets.myskills.server.MySkillsServerApplicationTests;
import io.knowledgeassets.myskills.server.common.Neo4jSessionFactoryConfiguration;
import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.query.UserSkillPriorityAggregationReportQueryService;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReportRepository;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.command.UserSkillPriorityReportCommandService;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.query.UserSkillPriorityReportQueryService;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReport;
import io.knowledgeassets.myskills.server.skill.SkillRepository;
import io.knowledgeassets.myskills.server.skill.query.SkillQueryController;
import io.knowledgeassets.myskills.server.user.UserRepository;

import io.knowledgeassets.myskills.server.userskill.UserSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class UserSkillPriorityReportCommandServiceTests extends MySkillsServerApplicationTests {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SkillRepository skillRepository;
	@Autowired
	private UserSkillRepository userSkillRepository;
	@Autowired
	private UserSkillPriorityReportRepository userSkillPriorityReportRepository;
	@Autowired
	private UserSkillPriorityReportQueryService userSkillPriorityReportQueryService;
	@Autowired
	private UserSkillPriorityReportCommandService userSkillPriorityReportCommandService;
	@Autowired
	private UserSkillPriorityAggregationReportQueryService userSkillPriorityAggregationReportQueryService;
	@Autowired
	private InitializeDataForReport initializeDataForReport;

	private UserSkillPriorityReport report;

	@BeforeEach
	void init() {
		initializeDataForReport.createData();
		report = userSkillPriorityReportCommandService.createPriorityReport();
	}

	@Test
	public void createReport() {
		assertEquals(2, userRepository.count());
		assertEquals(4, skillRepository.count());
		assertEquals(7, userSkillRepository.count());

		assertEquals(1, userSkillPriorityReportRepository.count());

		List<UserSkillPriorityAggregationReport> userSkillPriorityAggregationReports = null;
		try {
			userSkillPriorityAggregationReports = userSkillPriorityAggregationReportQueryService
					.getUserSkillPriorityAggregationReportsByReportId(report.getId())
					.collect(Collectors.toList());
		} catch (BusinessException e) {
			fail(e);
		}

		assertEquals(3, userSkillPriorityAggregationReports.size());
		UserSkillPriorityAggregationReport angular = userSkillPriorityAggregationReports.stream()
				.filter(userSkillPriorityAggregationReport ->
						userSkillPriorityAggregationReport.getSkillName().equals("Angular")
				).collect(Collectors.toList()).get(0);

		assertThat(angular.getAveragePriority()).isEqualTo(1.5);
		assertThat(angular.getMaximumPriority()).isEqualTo(2);
		assertThat(angular.getUserCount()).isEqualTo(2);

		List<UserSkillReport> userSkillReports = angular.getUserSkillReports();
		UserSkillReport tester2 = userSkillReports.stream()
				.filter(userSkillReport ->
						userSkillReport.getUserName().equals("tester2")
				).collect(Collectors.toList()).get(0);;

		assertThat(tester2.getCurrentLevel()).isEqualTo(2);
		assertThat(tester2.getDesiredLevel()).isEqualTo(4);
		assertThat(tester2.getPriority()).isEqualTo(1);
	}

}
