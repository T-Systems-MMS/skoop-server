package io.knowledgeassets.myskills.server.report.userskillpriorityreport.query;

import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.exception.EmptyInputException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.report.UserSkillPriorityAggregationReportResult;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReportRepository;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReport;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.userskill.query.UserSkillQueryService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserSkillPriorityReportQueryService {

	private UserSkillPriorityReportRepository userSkillPriorityReportRepository;
	private UserSkillQueryService userSkillQueryService;

	public UserSkillPriorityReportQueryService(UserSkillPriorityReportRepository userSkillPriorityReportRepository,
											   UserSkillQueryService userSkillQueryService) {
		this.userSkillPriorityReportRepository = userSkillPriorityReportRepository;
		this.userSkillQueryService = userSkillQueryService;
	}

	/**
	 * It return data for creating a report in {@link io.knowledgeassets.myskills.server.report.userskillpriorityreport.UserSkillPriorityReport} entity.
	 *
	 * @return
	 */
	@Transactional(readOnly = true)
	public Stream<UserSkillPriorityAggregationReportResult> getPrioritizedSkillsToCreateReport() {
		return userSkillQueryService.findPrioritizedSkillsToCreateReport();
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillPriorityReport> getAllReports() {
		return StreamSupport.stream(userSkillPriorityReportRepository.findAll(orderByDate()).spliterator(), false);
	}

	private Sort orderByDate() {
		return new Sort(Sort.Direction.DESC, "date");
	}

	/**
	 * If it finds the entity with the input id parameter, it will return it, otherwise it returns and exception.
	 *
	 * @param userSkillPriorityReportId
	 * @return
	 * @throws BusinessException
	 */
	@Transactional(readOnly = true)
	public UserSkillPriorityReport getById(String userSkillPriorityReportId) throws BusinessException {
		if (exists(userSkillPriorityReportId)) {
			return userSkillPriorityReportRepository.findById(userSkillPriorityReportId).get();
		} else {
			String[] searchParamsMap = {"id", userSkillPriorityReportId};
			throw NoSuchResourceException.builder()
					.model(Model.UserSkillPriorityReport)
					.searchParamsMap(searchParamsMap)
					.build();
		}
	}

	@Transactional(readOnly = true)
	public boolean exists(String userSkillPriorityReportId) throws EmptyInputException {
		if (userSkillPriorityReportId == null) {
			throw EmptyInputException.builder()
					.message("userSkillPriorityReportId is null.")
					.build();
		}
		return userSkillPriorityReportRepository.existsById(userSkillPriorityReportId);
	}

}
