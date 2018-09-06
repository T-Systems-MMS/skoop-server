package io.knowledgeassets.myskills.server.report.userreport;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReportRepository extends Neo4jRepository<UserReport, String> {

}
