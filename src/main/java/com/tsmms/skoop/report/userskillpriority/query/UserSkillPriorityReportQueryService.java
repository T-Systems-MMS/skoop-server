package com.tsmms.skoop.report.userskillpriority.query;

import com.tsmms.skoop.exception.EmptyInputException;
import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.report.userskillpriority.UserSkillPriorityAggregationReportResult;
import com.tsmms.skoop.report.userskillpriority.UserSkillPriorityReport;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.report.userskill.UserSkillReport;
import com.tsmms.skoop.report.userskillpriority.UserSkillPriorityAggregationReportRepository;
import com.tsmms.skoop.report.userskillpriority.UserSkillPriorityReportRepository;
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
	public UserSkillPriorityReport getReportById(String reportId) {
		return userSkillPriorityReportRepository.findById(reportId)
				.orElseThrow(() -> NoSuchResourceException.builder()
						.model(Model.USER_SKILL_PRIORITY_REPORT)
						.searchParamsMap(new String[]{"id", reportId})
						.build());
	}

	@Transactional(readOnly = true)
	public boolean existsReport(String reportId) {
		if (reportId == null) {
			throw EmptyInputException.builder()
					.message("reportId is null")
					.build();
		}
		return userSkillPriorityReportRepository.existsById(reportId);
	}

	@Transactional(readOnly = true)
	public boolean existsAggregationReport(String aggregationReportId) {
		if (aggregationReportId == null) {
			throw EmptyInputException.builder()
					.message("aggregationReportId is null.")
					.build();
		}
		return userSkillPriorityAggregationReportRepository.existsById(aggregationReportId);
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillReport> getUserSkillReportsByAggregationReportId(String aggregationReportId) {
		if (!existsAggregationReport(aggregationReportId)) {
			String[] searchParamsMap = {"id", aggregationReportId};
			throw NoSuchResourceException.builder()
					.model(Model.USER_SKILL_PRIORITY_AGGREGATION_REPORT)
					.searchParamsMap(searchParamsMap)
					.build();
		}
		return StreamSupport.stream(userSkillPriorityAggregationReportRepository
				.findUserSkillReportsByAggregationReportId(aggregationReportId).spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillPriorityAggregationReportResult> getAllUserSkillPriorityAggregationResults() {
		return StreamSupport.stream(userSkillPriorityAggregationReportRepository.findAllPrioritizedSkills().spliterator(), false);
	}
}
