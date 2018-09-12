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
					.code(10010L)
					.message("reportId is null")
					.build();
		}
		if (!userSkillPriorityReportQueryService.exists(reportId)) {
			String[] searchParamsMap = {"id", reportId};
			throw NoSuchResourceException.builder()
					.model(Model.UserSkillPriorityReport)
					.code(10011L)
					.searchParamsMap(searchParamsMap)
					.build();
		}
		return StreamSupport.stream(userSkillPriorityAggregationReportRepository.findPrioritizedSkillsForReport(reportId).spliterator(), false);
	}

	/**
	 * If it finds the entity with the input id parameter, it will return it, otherwise it returns and exception.
	 *
	 * @param userSkillPriorityAggregationReportId
	 * @return
	 * @throws BusinessException
	 */
	@Transactional(readOnly = true)
	public UserSkillPriorityAggregationReport getById(String userSkillPriorityAggregationReportId) throws BusinessException {
		if (exists(userSkillPriorityAggregationReportId)) {
			return userSkillPriorityAggregationReportRepository.findById(userSkillPriorityAggregationReportId).get();
		} else {
			String[] searchParamsMap = {"id", userSkillPriorityAggregationReportId};
			throw NoSuchResourceException.builder()
					.model(Model.UserSkillPriorityAggregationReport)
					.code(111111L)
					.searchParamsMap(searchParamsMap)
					.build();
		}
	}

	@Transactional(readOnly = true)
	public boolean exists(String userSkillPriorityAggregationReportId) throws EmptyInputException {
		if (userSkillPriorityAggregationReportId == null) {
			throw EmptyInputException.builder()
					.code(111111L)
					.message("userSkillPriorityAggregationReportId is null.")
					.build();
		}
		return userSkillPriorityAggregationReportRepository.existsById(userSkillPriorityAggregationReportId);
	}
}
