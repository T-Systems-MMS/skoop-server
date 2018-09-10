package io.knowledgeassets.myskills.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MySkillsServerApplication.class)
@Transactional
public class MySkillsServerApplicationTests {
	@Test
	@DisplayName("Application context loads successfully")
	void contextLoads() {
	}
}
