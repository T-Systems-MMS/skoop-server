package io.knowledgeassets.myskills.server.report.skillreport;

import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReport;
import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class SkillReportQueryService {

	private SkillReportRepository skillReportRepository;

	public SkillReportQueryService(SkillReportRepository skillReportRepository) {
		this.skillReportRepository = skillReportRepository;
	}

	@Transactional(readOnly = true)
	public Optional<SkillReport> getSkillReportById(String skillPriorityId) {
		return skillReportRepository.findById(skillPriorityId);
	}
}
