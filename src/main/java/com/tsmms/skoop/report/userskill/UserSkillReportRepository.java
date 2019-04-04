package com.tsmms.skoop.report.userskill;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSkillReportRepository extends Neo4jRepository<UserSkillReport, String> {
}
