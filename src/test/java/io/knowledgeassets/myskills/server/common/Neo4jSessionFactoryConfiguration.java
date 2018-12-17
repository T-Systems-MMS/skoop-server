package io.knowledgeassets.myskills.server.common;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;

/**
 * Test configuration providing a Neo4j {@link SessionFactory} for the application base package. This configuration is
 * required as a workaround to make {@link WebMvcTest} application contexts start correctly. Otherwise, those web slice
 * tests will fail to load the application context due to the missing Neo4j {@link SessionFactory} bean.
 */
public class Neo4jSessionFactoryConfiguration {
	@Bean
	public SessionFactory sessionFactory() {
		return new SessionFactory("io.knowledgeassets.myskills.server");
	}
}
