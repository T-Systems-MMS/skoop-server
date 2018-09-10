package io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport;

import io.knowledgeassets.myskills.server.report.UserSkillPriorityReportResult;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSkillPriorityAggregationReportRepository extends Neo4jRepository<UserSkillPriorityAggregationReport, String> {

	@Query("MATCH (detailsReport:UserSkillPriorityReport {id: {reportId}})-[:USER_SKILL_PRIORITY_AGGREGATION_REPORTS]-(skillReport:UserSkillPriorityAggregationReport) " +
			"RETURN skillReport")
	Iterable<UserSkillPriorityAggregationReport> findPrioritizedSkillsForReport(@Param("reportId") String reportId);

}
