package io.knowledgeassets.myskills.server.report.batch;

import io.knowledgeassets.myskills.server.report.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.skillpriorityreport.SkillPriorityReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.command.UserSkillPriorityReportCommandService;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.query.UserSkillPriorityReportQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * Scheduling UserSkillPriorityReport.
 * This scheduler creates <b>skill priorities userskillpriorityreport</b> every week on Sundays.
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
	 * Examples:
	 *
	 * @Scheduled(cron = "0 0 0 * * SUN")                    // This schedule starts every week on Sundays.
	 * @Scheduled(cron = "0 0 0 * * ?")                    // This schedule starts every day.
	 * @Scheduled(cron = "0 0 * * * ?")                    // This schedule starts every hour.
	 * @Scheduled(initialDelay = 0, fixedRate = 3600000)    // This schedule starts every hour.
	 */
	@Scheduled(initialDelay = 0, fixedRate = 3600000)    // This schedule starts every hour.
	public void reportCurrentTime() {
		userSkillPriorityReportCommandService.deleteAllPriorityReports();
		Stream<UserSkillPriorityAggregationReport> prioritizedSkillsForReport = read();
		List<SkillPriorityReport> skillPriorityReports = convert(prioritizedSkillsForReport);
		write(skillPriorityReports);
	}

	public Stream<UserSkillPriorityAggregationReport> read() {
		return userSkillPriorityReportQueryService.getPrioritizedSkillsToCreateReport();
	}

	public List<SkillPriorityReport> convert(Stream<UserSkillPriorityAggregationReport> prioritizedSkillsForReport) {
		return userSkillPriorityReportCommandService.convert(prioritizedSkillsForReport);
	}

	public void write(List<SkillPriorityReport> skillPriorityReports) {
		userSkillPriorityReportCommandService.createPriorityReport(skillPriorityReports);
	}
}
