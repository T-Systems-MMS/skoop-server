package io.knowledgeassets.myskills.server.report.batch;

import io.knowledgeassets.myskills.server.report.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.report.command.UserSkillPriorityReportCommandService;
import io.knowledgeassets.myskills.server.report.report.query.UserSkillPriorityReportQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Scheduling UserSkillPriorityReport.
 * This scheduler creates <b>skill priorities report</b> every week on Sundays.
 */
@Component
@Slf4j
public class ScheduledTasks {

	private UserSkillPriorityReportQueryService userSkillPriorityReportQueryService;
	private UserSkillPriorityReportCommandService userSkillPriorityReportCommandService;

	public ScheduledTasks(UserSkillPriorityReportQueryService userSkillPriorityReportQueryService, UserSkillPriorityReportCommandService userSkillPriorityReportCommandService) {
		this.userSkillPriorityReportQueryService = userSkillPriorityReportQueryService;
		this.userSkillPriorityReportCommandService = userSkillPriorityReportCommandService;
	}

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	/**
	 * second, minute, hour, day of month, month, day of week
	 */
//	@Scheduled(cron = "0 0 0 * * SUN")                    // This schedule starts every week on Sunday.
//	@Scheduled(cron = "0 0 * * * ?")                    // This schedule starts every every hour.
	@Scheduled(initialDelay = 0, fixedRate = 3600000)    // This schedule starts every every hour.
	public void reportCurrentTime() {
//		Stream<UserSkillPriorityAggregationReport> prioritizedSkillsForReport = userSkillPriorityReportQueryService.getPrioritizedSkillsToCreateReport();
//		System.out.println("Count of records: " + prioritizedSkillsForReport.count());
//		userSkillPriorityReportCommandService.deleteAllPriorityReports();
		Stream<UserSkillPriorityAggregationReport> prioritizedSkillsForReport = userSkillPriorityReportQueryService.getPrioritizedSkillsToCreateReport();
		userSkillPriorityReportCommandService.createPriorityReport(prioritizedSkillsForReport);
		log.info("The time is now {}", dateFormat.format(new Date()));
	}
}
