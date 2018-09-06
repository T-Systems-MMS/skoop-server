package io.knowledgeassets.myskills.server.report.skillreport;

import io.knowledgeassets.myskills.server.report.userreport.UserReport;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillReportRepository extends Neo4jRepository<SkillReport, String> {

}
