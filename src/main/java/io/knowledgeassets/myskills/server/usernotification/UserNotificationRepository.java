package io.knowledgeassets.myskills.server.usernotification;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationRepository extends Neo4jRepository<UserNotification, String> {
}
