package io.knowledgeassets.myskills.server.report.skillpriorityreport;

import io.knowledgeassets.myskills.server.report.UserSkillPriorityReportResult;
import io.knowledgeassets.myskills.server.report.userreport.UserReport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class SkillPriorityReportQueryService {

	private SkillPriorityReportRepository skillPriorityReportRepository;

	public SkillPriorityReportQueryService(SkillPriorityReportRepository skillPriorityReportRepository) {
		this.skillPriorityReportRepository = skillPriorityReportRepository;
	}

	@Transactional(readOnly = true)
	public Stream<UserSkillPriorityReportResult> getUserSkillPriorityReportDetailsByReportId(String reportId) {
		return StreamSupport.stream(skillPriorityReportRepository.findPrioritizedSkillsForReport(reportId).spliterator(), false);
	}

	@Transactional(readOnly = true)
	public Long countBySkillReportId(String reportId) {
		return skillPriorityReportRepository.countByUserSkillPriorityReportId(reportId);
	}
}
