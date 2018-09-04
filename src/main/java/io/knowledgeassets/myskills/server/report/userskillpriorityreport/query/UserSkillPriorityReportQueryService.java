package io.knowledgeassets.myskills.server.report.userskillpriorityreport.query;

import io.knowledgeassets.myskills.server.report.userskillprioritydetailsreport.UserSkillPriorityDetailsReport;
import io.knowledgeassets.myskills.server.report.userskillprioritydetailsreport.UserSkillPriorityReportDetailsRepository;
import io.knowledgeassets.myskills.server.report.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReportRepository;
import io.knowledgeassets.myskills.server.userskill.query.UserSkillQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserSkillPriorityReportQueryService {

	private UserSkillPriorityReportDetailsRepository userSkillPriorityReportDetailsRepository;
	private UserSkillQueryService userSkillQueryService;
	private UserSkillPriorityReportRepository userSkillPriorityReportRepository;

	public UserSkillPriorityReportQueryService(UserSkillPriorityReportRepository userSkillPriorityReportRepository, UserSkillPriorityReportDetailsRepository userSkillPriorityReportDetailsRepository, UserSkillQueryService userSkillQueryService) {
		this.userSkillPriorityReportDetailsRepository = userSkillPriorityReportDetailsRepository;
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

	@Transactional(readOnly = true)
	public Optional<UserSkillPriorityDetailsReport> getUserSkillPriorityReportDetailsByReportId(String reportId) {
		return userSkillPriorityReportDetailsRepository.findById(reportId);
	}
}
