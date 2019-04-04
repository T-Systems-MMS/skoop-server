package com.tsmms.skoop.report.userskillpriority;

import com.tsmms.skoop.report.userskill.UserSkillReport;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSkillPriorityAggregationReportRepository extends Neo4jRepository<UserSkillPriorityAggregationReport, String> {
	@Query("MATCH (aggregationReport:UserSkillPriorityAggregationReport {id: {aggregationReportId}})-[:CONTAINS]-(userSkillReport:UserSkillReport) " +
			"RETURN userSkillReport")
	Iterable<UserSkillReport> findUserSkillReportsByAggregationReportId(@Param("aggregationReportId") String aggregationReportId);

	@Query("MATCH (skill:Skill)-[userSkill:RELATED_TO]-(user:User) " +
			"WHERE userSkill.priority > 0 " +
			"RETURN skill AS skill, AVG(userSkill.priority) AS averagePriority, " +
			"MAX(userSkill.priority) AS maximumPriority, COUNT(*) AS userCount, COLLECT(user) AS users " +
			"ORDER BY AVG(userSkill.priority) DESC, COUNT(*) DESC, MAX(userSkill.priority) DESC ")
	Iterable<UserSkillPriorityAggregationReportResult> findAllPrioritizedSkills();
}
