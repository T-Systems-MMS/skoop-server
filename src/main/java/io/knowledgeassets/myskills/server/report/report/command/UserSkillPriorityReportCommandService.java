package io.knowledgeassets.myskills.server.report.report.command;

import io.knowledgeassets.myskills.server.report.priorityreportdetails.UserSkillPriorityReportDetails;
import io.knowledgeassets.myskills.server.report.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.report.UserSkillPriorityReport;
import io.knowledgeassets.myskills.server.report.report.UserSkillPriorityReportRepository;
import io.knowledgeassets.myskills.server.report.skillreport.SkillReport;
import io.knowledgeassets.myskills.server.report.userreport.UserReport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserSkillPriorityReportCommandService {

	private UserSkillPriorityReportRepository userSkillPriorityReportRepository;

	public UserSkillPriorityReportCommandService(UserSkillPriorityReportRepository userSkillPriorityReportRepository) {
		this.userSkillPriorityReportRepository = userSkillPriorityReportRepository;
	}

	@Transactional
	public void createPriorityReport(Stream<UserSkillPriorityAggregationReport> userSkillPriorityAggregationReportStream) {
		UserSkillPriorityReport userSkillPriorityReport = UserSkillPriorityReport.builder()
				.id(UUID.randomUUID().toString())
				.date(LocalDateTime.now())
				.build();
		Set<UserSkillPriorityReportDetails> userSkillPriorityReportDetails = new HashSet<>();
		userSkillPriorityAggregationReportStream.forEach(userSkillPriorityAggregationReport ->
				userSkillPriorityReportDetails.add(UserSkillPriorityReportDetails.builder()
						.id(UUID.randomUUID().toString())
						.averagePriority(userSkillPriorityAggregationReport.getAveragePriority())
						.maximumPriority(userSkillPriorityAggregationReport.getMaximumPriority())
						.userCount(userSkillPriorityAggregationReport.getUserCount())
						.skillReport(SkillReport.builder()
								.id(UUID.randomUUID().toString())
								.name(userSkillPriorityAggregationReport.getSkill().getName())
								.description(userSkillPriorityAggregationReport.getSkill().getDescription())
								.build()
						).userReports(
								userSkillPriorityAggregationReport.getUsers().stream().map(
										user -> UserReport.builder()
												.id(UUID.randomUUID().toString())
												.userName(user.getUserName())
												.firstName(user.getFirstName())
												.lastName(user.getLastName())
												.email(user.getEmail())
												.build()
								).collect(Collectors.toSet())
						)
						.build())
		);
		userSkillPriorityReport.setUserSkillPriorityReportDetails(userSkillPriorityReportDetails);
		userSkillPriorityReportRepository.save(userSkillPriorityReport);
	}

	@Transactional
	public void deleteAllPriorityReports() {
		userSkillPriorityReportRepository.deleteAll();
	}
}
