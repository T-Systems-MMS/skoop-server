package io.knowledgeassets.myskills.server.common;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.support.Neo4jAuditingBeanFactoryPostProcessor;

/**
 * Test configuration providing a Neo4j {@link SessionFactory} for the application base package. This configuration is
 * required as a workaround to make {@link WebMvcTest} application contexts start correctly. The origin of the problem
 * is the {@link Neo4jAuditingBeanFactoryPostProcessor} which requires a session factory in the constructor. This does
 * not seem to work well with the "simplified" application context in Web/MVC tests. :-(
 */
public class Neo4jSessionFactoryConfiguration {
	@Bean
	public SessionFactory sessionFactory() {
		return new SessionFactory("io.knowledgeassets.myskills.server");
	}
}
