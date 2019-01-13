package io.knowledgeassets.myskills.server;

import io.knowledgeassets.myskills.server.neo4j.IndexManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MySkillsServerApplicationTests {

	@MockBean
	private IndexManager mockIndexManager;

	@Test
	@DisplayName("Application context loads successfully")
	void contextLoads() {
	}

	@Test
	public void relatedProceduresAreRunning() {
		verify(mockIndexManager).createIndexes();
	}
}
