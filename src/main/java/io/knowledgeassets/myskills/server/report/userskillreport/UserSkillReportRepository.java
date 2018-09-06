package io.knowledgeassets.myskills.server.report.userskillreport;

import io.knowledgeassets.myskills.server.report.skillreport.SkillReport;
import io.knowledgeassets.myskills.server.userskill.UserSkillPriorityAggregation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSkillReportRepository extends Neo4jRepository<UserSkillReport, String> {

	@Query("MATCH (skillReport:SkillReport {id: {skillId}})-[related:SKILL_USER_RELATED_TO]-(userReport:UserReport) " +
			"RETURN related.currentLevel AS currentLevel, related.desiredLevel  AS desiredLevel, related.priority AS priority, userReport ")
	Iterable<UserSkillReportAggregation> findUserSkillReportById(@Param("skillId") String skillId);

}
