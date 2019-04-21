package com.tsmms.skoop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * Due to the fact that embedded containers do not support @WebServlet, @WebFilter and @WebListener annotations, Spring
 * Boot, relying greatly on embedded containers, introduced this new annotation @ServletComponentScan to support some
 * dependent jars that use these 3 annotations.
 */
@ServletComponentScan
@SpringBootApplication
@EnableNeo4jRepositories
@EnableTransactionManagement
@Transactional(rollbackFor = Exception.class)
@EnableCaching
public class SkoopServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SkoopServerApplication.class, args);
	}
}
