package io.knowledgeassets.myskills.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MySkillsServerApplicationTests {
	@Test
	@DisplayName("Application context loads successfully")
	void contextLoads() {
	}
}
