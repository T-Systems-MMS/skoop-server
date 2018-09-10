package io.knowledgeassets.myskills.server.report.userskillreport;

import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.UserSkillPriorityAggregationReport;
import io.knowledgeassets.myskills.server.userskill.UserSkillPriorityAggregation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSkillReportRepository extends Neo4jRepository<UserSkillReport, String> {

	@Query("MATCH (skillReport:UserSkillPriorityAggregationReport {id: {userSkillPriorityAggregationReportId}})-[:USER_SKILL_REPORTS]-(userSkillReport:UserSkillReport) " +
			"RETURN userSkillReport")
	Iterable<UserSkillReport> findUsersByUserSkillPriorityAggregationReportId(@Param("userSkillPriorityAggregationReportId") String userSkillPriorityAggregationReportId);

}
