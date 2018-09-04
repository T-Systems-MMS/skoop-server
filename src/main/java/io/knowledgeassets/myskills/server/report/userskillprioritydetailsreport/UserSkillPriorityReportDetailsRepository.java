package io.knowledgeassets.myskills.server.report.userskillprioritydetailsreport;

import io.knowledgeassets.myskills.server.report.UserSkillPriorityAggregationReport;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSkillPriorityReportDetailsRepository extends Neo4jRepository<UserSkillPriorityDetailsReport, String> {

	@Query("MATCH (skillReport:SkillReport)-[have:HAVE]-(userReport:UserReport) " +
			"WHERE userSkill.priority > 0 " +
			"RETURN skill AS skill, AVG(userSkill.priority) AS averagePriority, collect (user) AS users, " +
			"MAX(userSkill.priority) AS maximumPriority, COUNT(*) AS userCount " +
			"ORDER BY AVG(userSkill.priority) DESC, COUNT(*) DESC, MAX(userSkill.priority) DESC ")
	Iterable<UserSkillPriorityAggregationReport> findPrioritizedSkillsForReport();
}
