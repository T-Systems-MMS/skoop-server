package io.knowledgeassets.myskills.server.report.userskillpriorityreport.command;

import io.knowledgeassets.myskills.server.report.UserSkillPriorityAggregationReportResult;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReportRepository;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.query.UserSkillPriorityReportQueryService;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReport;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import io.knowledgeassets.myskills.server.userskill.UserSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
public class UserSkillPriorityReportCommandService {

	private UserSkillPriorityReportRepository userSkillPriorityReportRepository;
	private UserSkillRepository userSkillRepository;
	private UserSkillPriorityReportQueryService userSkillPriorityReportQueryService;

	public UserSkillPriorityReportCommandService(UserSkillPriorityReportRepository userSkillPriorityReportRepository,
												 UserSkillRepository userSkillRepository,
												 UserSkillPriorityReportQueryService userSkillPriorityReportQueryService) {
		this.userSkillPriorityReportRepository = userSkillPriorityReportRepository;
		this.userSkillRepository = userSkillRepository;
		this.userSkillPriorityReportQueryService = userSkillPriorityReportQueryService;
	}

	/**
	 * Create a report
	 * for creating a report we need to do below steps:
	 * 1- read data from existing data.
	 * 2- convert it to List<UserSkillPriorityAggregationReport> and after that assign it to a UserSkillPriorityReport object.
	 * 3- save the UserSkillPriorityReport object.
	 * @return
	 */
	@Transactional
	public UserSkillPriorityReport createPriorityReport() {
//		deleteAllPriorityReports();
		Stream<UserSkillPriorityAggregationReportResult> prioritizedSkillsForReport = read();
		List<UserSkillPriorityAggregationReport> userSkillPriorityAggregationReports = convert(prioritizedSkillsForReport);
		return saveReport(userSkillPriorityAggregationReports);
	}

	private Stream<UserSkillPriorityAggregationReportResult> read() {
		return userSkillPriorityReportQueryService.getPrioritizedSkillsToCreateReport();
	}

	private List<UserSkillPriorityAggregationReport> convert(Stream<UserSkillPriorityAggregationReportResult> userSkillPriorityAggregationReportStream) {
		List<UserSkillPriorityAggregationReport> userSkillPriorityAggregationReports = new ArrayList<>();
		userSkillPriorityAggregationReportStream.forEach(userSkillPriorityAggregationReport -> {
					userSkillPriorityAggregationReports.add(UserSkillPriorityAggregationReport.builder()
							.id(UUID.randomUUID().toString())
							.averagePriority(userSkillPriorityAggregationReport.getAveragePriority())
							.maximumPriority(userSkillPriorityAggregationReport.getMaximumPriority())
							.userCount(userSkillPriorityAggregationReport.getUserCount())
							.skillName(userSkillPriorityAggregationReport.getSkill().getName())
							.skillDescription(userSkillPriorityAggregationReport.getSkill().getDescription())
							.userSkillReports(setUsers(userSkillPriorityAggregationReport))
							.build());
				}
		);
		return userSkillPriorityAggregationReports;
	}

	private List<UserSkillReport> setUsers(UserSkillPriorityAggregationReportResult userSkillPriorityAggregationReport) {
		List<UserSkillReport> userSkillReports = new ArrayList<>();
		for (User user : userSkillPriorityAggregationReport.getUsers()) {
			Optional<UserSkill> byUserIdAndSkillName = userSkillRepository
					.findByUserIdAndSkillName(user.getId(), userSkillPriorityAggregationReport.getSkill().getName());

			if (byUserIdAndSkillName.isPresent()) {
				UserSkill userSkill = byUserIdAndSkillName.get();
				userSkillReports.add(UserSkillReport.builder()
						.id(UUID.randomUUID().toString())
						.currentLevel(userSkill.getCurrentLevel())
						.desiredLevel(userSkill.getDesiredLevel())
						.priority(userSkill.getPriority())
						.userName(user.getUserName())
						.skillName(userSkillPriorityAggregationReport.getSkill().getName())
						.build()
				);
			}
		}
		return userSkillReports;
	}


	private UserSkillPriorityReport saveReport(List<UserSkillPriorityAggregationReport> userSkillPriorityAggregationReports) {
		UserSkillPriorityReport userSkillPriorityReport = UserSkillPriorityReport.builder()
				.id(UUID.randomUUID().toString())
				.date(LocalDateTime.now())
				.build();

		userSkillPriorityReport.setUserSkillPriorityAggregationReports(userSkillPriorityAggregationReports);
		userSkillPriorityReportRepository.save(userSkillPriorityReport);
		return userSkillPriorityReport;
	}

	@Transactional
	public void deleteAllPriorityReports() {
		userSkillPriorityReportRepository.deleteAll();
	}
}
