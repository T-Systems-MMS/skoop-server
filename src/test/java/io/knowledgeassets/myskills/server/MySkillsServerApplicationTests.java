package io.knowledgeassets.myskills.server;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.configuration.BoltConnector;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MySkillsServerApplicationTests {

	private static Path neo4jDirectory;
	private static GraphDatabaseService graphDatabaseService;

	/**
	 * Starts up the embedded (file-based in a temporary directory) Neo4j with bolt connector available to enable Liquigraph.
	 * @throws IOException - in case of I/O error.
	 */
	@BeforeAll
	static void setUp() throws IOException {
		final BoltConnector bolt = new BoltConnector( "bolt" );
		neo4jDirectory = Files.createTempDirectory("neo4jTmpEmbedded.database.");
		graphDatabaseService = new GraphDatabaseFactory()
				.newEmbeddedDatabaseBuilder(neo4jDirectory.toFile())
				.setConfig( bolt.type, "BOLT" )
				.setConfig( bolt.enabled, "true" )
				.setConfig( bolt.listen_address, "localhost:7324" )
				.newGraphDatabase();
	}

	@Test
	@DisplayName("Application context loads successfully")
	void contextLoads() {
	}

	/**
	 * Shuts down the embedded Neo4j and remove its directory.
	 * @throws IOException - in case of I/O error.
	 */
	@AfterAll
	static void cleanUp() throws IOException {
		if (graphDatabaseService != null) {
			graphDatabaseService.shutdown();
		}
		if (neo4jDirectory != null && neo4jDirectory.toFile().exists()) {
			FileSystemUtils.deleteRecursively(neo4jDirectory);
		}
	}

}
