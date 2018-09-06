package io.knowledgeassets.myskills.server.report.userskillreport.query;

import io.knowledgeassets.myskills.server.report.UserSkillPriorityReportResult;
import io.knowledgeassets.myskills.server.report.skillreport.SkillReport;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReportAggregation;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserSkillReportQueryService {

	private UserSkillReportRepository userSkillReportRepository;

	public UserSkillReportQueryService(UserSkillReportRepository userSkillReportRepository) {
		this.userSkillReportRepository = userSkillReportRepository;
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillReportAggregation> getUserSkillReportById(String skillId) {
		return StreamSupport.stream(userSkillReportRepository.findUserSkillReportById(skillId).spliterator(), false);
	}

}
