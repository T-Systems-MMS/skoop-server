package com.tsmms.skoop.neo4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.neo4j.ogm.config.Configuration.Builder;

@Configuration
public class SkoopNeo4jConfiguration {

	@Bean
	public org.neo4j.ogm.config.Configuration configuration(
			@Value("${spring.data.neo4j.connectionLivenessCheckTimeout:0}") Integer connectionLivenessCheckTimeout,
			Neo4jProperties properties) {
		final Builder builder = new Builder();
		org.neo4j.ogm.config.Configuration configuration = properties.createConfiguration();
		builder.autoIndex(configuration.getAutoIndex().getName());
		builder.credentials(properties.getUsername(), properties.getPassword());
		builder.uri(configuration.getURI());
		builder.connectionLivenessCheckTimeout(connectionLivenessCheckTimeout);
		return builder.build();
	}

}
