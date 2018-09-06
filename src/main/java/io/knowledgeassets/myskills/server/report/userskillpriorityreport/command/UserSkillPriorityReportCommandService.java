package io.knowledgeassets.myskills.server.report.userskillpriorityreport.command;

import io.knowledgeassets.myskills.server.report.userreport.UserReport;
import io.knowledgeassets.myskills.server.report.skillpriorityreport.SkillPriorityReport;
import io.knowledgeassets.myskills.server.report.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReportRepository;
import io.knowledgeassets.myskills.server.report.skillreport.SkillReport;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReport;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserRepository;
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

	public UserSkillPriorityReportCommandService(UserSkillPriorityReportRepository userSkillPriorityReportRepository,
												 UserSkillRepository userSkillRepository) {
		this.userSkillPriorityReportRepository = userSkillPriorityReportRepository;
		this.userSkillRepository = userSkillRepository;
	}

	@Transactional
	public void createPriorityReport(Stream<UserSkillPriorityAggregationReport> userSkillPriorityAggregationReportStream) {

		UserSkillPriorityReport userSkillPriorityReport = UserSkillPriorityReport.builder()
				.id(UUID.randomUUID().toString())
				.date(LocalDateTime.now())
				.build();

		List<SkillPriorityReport> skillPriorityReports = new ArrayList<>();

		userSkillPriorityAggregationReportStream.forEach(userSkillPriorityAggregationReport -> {
					skillPriorityReports.add(SkillPriorityReport.builder()
							.id(UUID.randomUUID().toString())
							.averagePriority(userSkillPriorityAggregationReport.getAveragePriority())
							.maximumPriority(userSkillPriorityAggregationReport.getMaximumPriority())
							.userCount(userSkillPriorityAggregationReport.getUserCount())
							.skillReport(createSkillReport(userSkillPriorityAggregationReport)
							)
							.userSkillPriorityReport(userSkillPriorityReport)
							.build());
				}
		);
		userSkillPriorityReport.setSkillPriorityReports(skillPriorityReports);
		userSkillPriorityReportRepository.save(userSkillPriorityReport);

	}

	private SkillReport createSkillReport(UserSkillPriorityAggregationReport userSkillPriorityAggregationReport) {
		SkillReport skillReport = SkillReport.builder()
				.id(UUID.randomUUID().toString())
				.name(userSkillPriorityAggregationReport.getSkill().getName())
				.description(userSkillPriorityAggregationReport.getSkill().getDescription())
				.build();
		skillReport.setUserSkillReports(createUserSkillReports(skillReport, userSkillPriorityAggregationReport.getUsers()));
		return skillReport;
	}

	private List<UserSkillReport> createUserSkillReports(SkillReport skillReport, List<User> users) {
		List<UserSkillReport> userSkillReports = new ArrayList<>();

		users.forEach(user -> {
			UserSkillReport userSkillReport = UserSkillReport.builder()
					.id(UUID.randomUUID().toString())
					.skillReport(skillReport)
					.userReport(UserReport.builder()
							.id(UUID.randomUUID().toString())
							.userName(user.getUserName())
							.firstName(user.getFirstName())
							.lastName(user.getLastName())
							.email(user.getEmail())
							.build())
					.build();
			Optional<UserSkill> byUserIdAndSkillName = userSkillRepository.findByUserIdAndSkillName(user.getId(), skillReport.getName());
			if (byUserIdAndSkillName.isPresent()) {
				UserSkill userSkill = byUserIdAndSkillName.get();
				userSkillReport.setCurrentLevel(userSkill.getCurrentLevel());
				userSkillReport.setDesiredLevel(userSkill.getDesiredLevel());
				userSkillReport.setPriority(userSkill.getPriority());
			} else {

			}

			userSkillReports.add(userSkillReport);
		});
		return userSkillReports;
	}

}
