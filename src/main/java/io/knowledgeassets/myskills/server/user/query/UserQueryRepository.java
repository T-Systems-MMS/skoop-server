package io.knowledgeassets.myskills.server.user.query;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserQueryRepository extends Neo4jRepository<User, String> {
	Optional<User> findByUserName(String userName);
}
