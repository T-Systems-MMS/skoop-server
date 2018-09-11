package io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.query;

import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.exception.EmptyInputException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityAggregationReportRepository;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.query.UserSkillPriorityReportQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserSkillPriorityAggregationReportQueryService {

	private UserSkillPriorityAggregationReportRepository userSkillPriorityAggregationReportRepository;
	private UserSkillPriorityReportQueryService userSkillPriorityReportQueryService;

	public UserSkillPriorityAggregationReportQueryService(UserSkillPriorityAggregationReportRepository userSkillPriorityAggregationReportRepository,
														  UserSkillPriorityReportQueryService userSkillPriorityReportQueryService) {
		this.userSkillPriorityAggregationReportRepository = userSkillPriorityAggregationReportRepository;
		this.userSkillPriorityReportQueryService = userSkillPriorityReportQueryService;
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillPriorityAggregationReport> getUserSkillPriorityAggregationReportsByReportId(String reportId) throws BusinessException {
		if (reportId == null) {
			throw EmptyInputException.builder()
					.message("reportId is null")
					.code(10010L)
					.build();
		}
		if (!userSkillPriorityReportQueryService.getById(reportId).isPresent()) {
			String[] searchParamsMap = {"reportId", reportId};
			throw NoSuchResourceException.builder()
					.model(Model.UserSkillPriorityReport)
					.code(10011L)
					.searchParamsMap(searchParamsMap)
					.build();
		}
		return StreamSupport.stream(userSkillPriorityAggregationReportRepository.findPrioritizedSkillsForReport(reportId).spliterator(), false);
	}

}
