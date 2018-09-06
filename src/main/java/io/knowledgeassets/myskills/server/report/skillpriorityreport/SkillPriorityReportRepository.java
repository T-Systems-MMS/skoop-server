package io.knowledgeassets.myskills.server.report.skillpriorityreport;

import io.knowledgeassets.myskills.server.report.UserSkillPriorityReportResult;
import io.knowledgeassets.myskills.server.report.userreport.UserReport;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillPriorityReportRepository extends Neo4jRepository<SkillPriorityReport, String> {

	Long countByUserSkillPriorityReportId(String reportId);

	@Query("MATCH (detailsReport:UserSkillPriorityReport {id: {reportId}})-[related:REPORT_SKILL_RELATED_TO]-(skillReport:SkillReport) " +
			"RETURN related.averagePriority AS averagePriority, related.maximumPriority  AS maximumPriority, related.userCount AS userCount, skillReport AS skillReport ")
	Iterable<UserSkillPriorityReportResult> findPrioritizedSkillsForReport(@Param("reportId") String reportId);

}
