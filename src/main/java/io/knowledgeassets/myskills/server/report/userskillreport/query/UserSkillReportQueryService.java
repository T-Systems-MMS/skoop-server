package io.knowledgeassets.myskills.server.report.userskillreport.query;

import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReport;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReportAggregation;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserSkillReportQueryService {

	private UserSkillReportRepository userSkillReportRepository;

	public UserSkillReportQueryService(UserSkillReportRepository userSkillReportRepository) {
		this.userSkillReportRepository = userSkillReportRepository;
	}

	/**
	 * this method get a UserSkillPriorityAggregationReportId, and return all the users along with user skill information.
	 *
	 * @param userSkillPriorityAggregationReportId
	 * @return
	 */
	@Transactional(readOnly = true)
	public Stream<UserSkillReport> getUsersByUserSkillPriorityAggregationReportId(String userSkillPriorityAggregationReportId) {
		return StreamSupport.stream(userSkillReportRepository.findUsersByUserSkillPriorityAggregationReportId(userSkillPriorityAggregationReportId).spliterator(), false);
	}

}
