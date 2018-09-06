package io.knowledgeassets.myskills.server.report.userreport.query;

import io.knowledgeassets.myskills.server.report.skillreport.SkillReport;
import io.knowledgeassets.myskills.server.report.skillreport.SkillReportRepository;
import io.knowledgeassets.myskills.server.report.userreport.UserReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserReportQueryService {

	private UserReportRepository userReportRepository;

	public UserReportQueryService(UserReportRepository userReportRepository) {
		this.userReportRepository = userReportRepository;
	}

}
