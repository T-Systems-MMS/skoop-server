package io.knowledgeassets.myskills.server.report.userskillpriorityreport.query;

import io.knowledgeassets.myskills.server.report.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReportRepository;
import io.knowledgeassets.myskills.server.userskill.query.UserSkillQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserSkillPriorityReportQueryService {

	private UserSkillQueryService userSkillQueryService;
	private UserSkillPriorityReportRepository userSkillPriorityReportRepository;

	public UserSkillPriorityReportQueryService(UserSkillPriorityReportRepository userSkillPriorityReportRepository,
											   UserSkillQueryService userSkillQueryService) {
		this.userSkillQueryService = userSkillQueryService;
		this.userSkillPriorityReportRepository = userSkillPriorityReportRepository;
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillPriorityAggregationReport> getPrioritizedSkillsToCreateReport() {
		return StreamSupport.stream(userSkillQueryService.findPrioritizedSkillsToCreateReport().spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillPriorityReport> getReports() {
		return StreamSupport.stream(userSkillPriorityReportRepository.findAll().spliterator(), false);
	}

}
