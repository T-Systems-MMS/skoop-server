package io.knowledgeassets.myskills.server.report.report.query;

import io.knowledgeassets.myskills.server.report.priorityreportdetails.UserSkillPriorityReportDetails;
import io.knowledgeassets.myskills.server.report.priorityreportdetails.UserSkillPriorityReportDetailsRepository;
import io.knowledgeassets.myskills.server.report.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.report.UserSkillPriorityReport;
import io.knowledgeassets.myskills.server.report.report.UserSkillPriorityReportRepository;
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
	public Optional<UserSkillPriorityReportDetails> getUserSkillPriorityReportDetailsByReportId(String reportId) {
		return userSkillPriorityReportDetailsRepository.findById(reportId);
	}
}
