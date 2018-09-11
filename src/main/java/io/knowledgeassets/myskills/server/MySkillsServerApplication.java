package io.knowledgeassets.myskills.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableNeo4jRepositories
@EnableTransactionManagement
@EnableSwagger2
@EnableScheduling
@Transactional(rollbackFor = Exception.class)
public class MySkillsServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MySkillsServerApplication.class, args);
	}
}
