package io.knowledgeassets.myskills.server.neo4j;

import org.neo4j.ogm.config.Configuration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

@ConfigurationProperties(
		prefix = "spring.data.neo4j"
)
@Component
@Primary
public class DumpNeo4jProperties extends Neo4jProperties {

	private String dumpDir;
	private String dumpFilename;

	@Override
	public Configuration createConfiguration() {
		Configuration.Builder builder = new Configuration.Builder();
		this.configure(builder);
		return builder.build();
	}

	private void configure(Configuration.Builder builder) {
		if (this.getUri() != null) {
			builder.uri(this.getUri());
		} else {
			this.configureUriWithDefaults(builder);
		}

		if (this.getUsername() != null && this.getPassword() != null) {
			builder.credentials(this.getUsername(), this.getPassword());
		}

		if (this.dumpDir != null) {
			builder.generatedIndexesOutputDir(this.dumpDir);
		}

		if (this.dumpFilename != null) {
			builder.generatedIndexesOutputFilename(this.dumpFilename);
		}

		builder.autoIndex(this.getAutoIndex().getName());
	}

	private void configureUriWithDefaults(Configuration.Builder builder) {
		if (!this.getEmbedded().isEnabled() || !ClassUtils.isPresent("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver", DumpNeo4jProperties.class.getClassLoader())) {
			builder.uri("bolt://localhost:7687");
		}
	}

	public String getDumpDir() {
		return dumpDir;
	}

	public void setDumpDir(String dumpDir) {
		this.dumpDir = dumpDir;
	}

	public String getDumpFilename() {
		return dumpFilename;
	}

	public void setDumpFilename(String dumpFilename) {
		this.dumpFilename = dumpFilename;
	}
}
