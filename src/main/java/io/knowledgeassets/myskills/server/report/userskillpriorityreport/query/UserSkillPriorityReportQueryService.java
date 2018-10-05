package io.knowledgeassets.myskills.server.report.userskillpriorityreport.query;

import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.exception.EmptyInputException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityAggregationReportRepository;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReportRepository;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReport;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserSkillPriorityReportQueryService {
	private UserSkillPriorityReportRepository userSkillPriorityReportRepository;
	private UserSkillPriorityAggregationReportRepository userSkillPriorityAggregationReportRepository;

	public UserSkillPriorityReportQueryService(UserSkillPriorityReportRepository userSkillPriorityReportRepository,
											   UserSkillPriorityAggregationReportRepository userSkillPriorityAggregationReportRepository) {
		this.userSkillPriorityReportRepository = userSkillPriorityReportRepository;
		this.userSkillPriorityAggregationReportRepository = userSkillPriorityAggregationReportRepository;
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillPriorityReport> getReports() {
		return StreamSupport.stream(userSkillPriorityReportRepository.findAll(orderByDate()).spliterator(), false);
	}

	private Sort orderByDate() {
		return new Sort(Sort.Direction.DESC, "date");
	}

	@Transactional(readOnly = true)
	public UserSkillPriorityReport getReportById(String reportId) throws BusinessException {
		return userSkillPriorityReportRepository.findById(reportId)
				.orElseThrow(() -> NoSuchResourceException.builder()
						.model(Model.UserSkillPriorityReport)
						.searchParamsMap(new String[]{"id", reportId})
						.build());
	}

	@Transactional(readOnly = true)
	public boolean existsReport(String reportId) throws EmptyInputException {
		if (reportId == null) {
			throw EmptyInputException.builder()
					.message("reportId is null")
					.build();
		}
		return userSkillPriorityReportRepository.existsById(reportId);
	}

	@Transactional(readOnly = true)
	public boolean existsAggregationReport(String aggregationReportId) throws EmptyInputException {
		if (aggregationReportId == null) {
			throw EmptyInputException.builder()
					.message("aggregationReportId is null.")
					.build();
		}
		return userSkillPriorityAggregationReportRepository.existsById(aggregationReportId);
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillReport> getUserSkillReportsByAggregationReportId(String aggregationReportId)
			throws BusinessException {
		if (!existsAggregationReport(aggregationReportId)) {
			String[] searchParamsMap = {"id", aggregationReportId};
			throw NoSuchResourceException.builder()
					.model(Model.UserSkillPriorityAggregationReport)
					.searchParamsMap(searchParamsMap)
					.build();
		}
		return StreamSupport.stream(userSkillPriorityAggregationReportRepository
				.findUserSkillReportsByAggregationReportId(aggregationReportId).spliterator(), false);
	}
}
