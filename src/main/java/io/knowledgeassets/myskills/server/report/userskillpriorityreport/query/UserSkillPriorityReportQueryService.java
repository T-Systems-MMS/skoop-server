package io.knowledgeassets.myskills.server.report.userskillpriorityreport.query;

import io.knowledgeassets.myskills.server.report.UserSkillPriorityAggregationReportResult;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReportRepository;
import io.knowledgeassets.myskills.server.userskill.query.UserSkillQueryService;
import org.springframework.data.domain.Sort;
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

	/**
	 * It return data for creating a report in {@link io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport} entity.
	 *
	 * @return
	 */
	@Transactional(readOnly = true)
	public Stream<UserSkillPriorityAggregationReportResult> getPrioritizedSkillsToCreateReport() {
		return StreamSupport.stream(userSkillQueryService.findPrioritizedSkillsToCreateReport().spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillPriorityReport> getAllReports() {
		return StreamSupport.stream(userSkillPriorityReportRepository.findAll(orderByDate()).spliterator(), false);
	}

	private Sort orderByDate() {
		return new Sort(Sort.Direction.DESC, "date");
	}

}
