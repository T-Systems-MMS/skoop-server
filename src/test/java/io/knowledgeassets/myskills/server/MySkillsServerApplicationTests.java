package io.knowledgeassets.myskills.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * We use this class as a parent for integration test classes.
 * Each test class that wants to do integration test, should extends this class.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = MySkillsServerApplication.class
)
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MySkillsServerApplicationTests {

	@Test
	@DisplayName("Application context loads successfully")
	void contextLoads() {
	}
}
