package io.knowledgeassets.myskills.server.report.userskillpriorityreport;

import io.knowledgeassets.myskills.server.report.userskillreport.UserSkillReport;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSkillPriorityAggregationReportRepository extends Neo4jRepository<UserSkillPriorityAggregationReport, String> {
	@Query("MATCH (aggregationReport:UserSkillPriorityAggregationReport {id: {aggregationReportId}})-[:CONTAINS]-(userSkillReport:UserSkillReport) " +
			"RETURN userSkillReport")
	Iterable<UserSkillReport> findUserSkillReportsByAggregationReportId(@Param("aggregationReportId") String aggregationReportId);

}
