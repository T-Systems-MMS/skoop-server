package io.knowledgeassets.myskills.server.report.userskillreport.query;

import io.knowledgeassets.myskills.server.exception.BusinessException;
import io.knowledgeassets.myskills.server.exception.EmptyInputException;
import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityAggregationReportRepository;
import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.query.UserSkillPriorityAggregationReportQueryService;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReport;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserSkillReportQueryService {

	private UserSkillReportRepository userSkillReportRepository;
	private UserSkillPriorityAggregationReportQueryService userSkillPriorityAggregationReportQueryService;

	public UserSkillReportQueryService(UserSkillReportRepository userSkillReportRepository, UserSkillPriorityAggregationReportQueryService userSkillPriorityAggregationReportQueryService) {
		this.userSkillReportRepository = userSkillReportRepository;
		this.userSkillPriorityAggregationReportQueryService = userSkillPriorityAggregationReportQueryService;
	}

	/**
	 * this method get a UserSkillPriorityAggregationReportId, and return all the users along with user skill information.
	 *
	 * @param userSkillPriorityAggregationReportId
	 * @return
	 */
	@Transactional(readOnly = true)
	public Stream<UserSkillReport> getUsersByUserSkillPriorityAggregationReportId(String userSkillPriorityAggregationReportId)
			throws BusinessException {
		if (!userSkillPriorityAggregationReportQueryService.exists(userSkillPriorityAggregationReportId)) {
			String[] searchParamsMap = {"id", userSkillPriorityAggregationReportId};
			throw NoSuchResourceException.builder()
					.model(Model.UserSkillPriorityAggregationReport)
					.code(10015L)
					.searchParamsMap(searchParamsMap)
					.build();
		}
		return StreamSupport.stream(userSkillReportRepository.findUsersByUserSkillPriorityAggregationReportId(userSkillPriorityAggregationReportId).spliterator(), false);
	}

	/**
	 * If it finds the entity with the input id parameter, it will return it, otherwise it returns and exception.
	 *
	 * @param userSkillReportId
	 * @return
	 * @throws BusinessException
	 */
	@Transactional(readOnly = true)
	public UserSkillReport getById(String userSkillReportId) throws BusinessException {
		if (exists(userSkillReportId)) {
			return userSkillReportRepository.findById(userSkillReportId).get();
		} else {
			String[] searchParamsMap = {"id", userSkillReportId};
			throw NoSuchResourceException.builder()
					.model(Model.UserSkillReport)
					.code(10013L)
					.searchParamsMap(searchParamsMap)
					.build();
		}
	}

	@Transactional(readOnly = true)
	public boolean exists(String userSkillReportId) throws EmptyInputException {
		if (userSkillReportId == null) {
			throw EmptyInputException.builder()
					.code(10014L)
					.message("userSkillReportId is null.")
					.build();
		}
		return userSkillReportRepository.existsById(userSkillReportId);
	}

}
