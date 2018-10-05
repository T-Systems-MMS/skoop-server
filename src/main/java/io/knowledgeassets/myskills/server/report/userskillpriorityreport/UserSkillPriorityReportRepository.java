package io.knowledgeassets.myskills.server.report.userskillpriorityreport;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSkillPriorityReportRepository extends Neo4jRepository<UserSkillPriorityReport, String> {
}
