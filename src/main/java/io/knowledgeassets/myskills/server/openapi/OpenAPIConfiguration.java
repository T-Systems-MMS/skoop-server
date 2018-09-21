package io.knowledgeassets.myskills.server.openapi;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;

import static java.util.Collections.emptyList;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;
import static springfox.documentation.swagger.web.UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS;

/**
 * Configuration for the generated Swagger UI providing the API documentation.
 */
@Configuration
public class OpenAPIConfiguration {
	// TODO: Extend Swagger UI configuration to offer OAuth 2.0 login

	@Bean
	public Docket mySkillsApi(@Value("${server.servlet.context-path:/}") String contextPath) {
		return new Docket(SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				// Exclude Spring Boot Actuator endpoints and error handlers.
				.paths(Predicates.not(PathSelectors.regex("^(/actuator|/error).*")))
				.build()
				// Configure the context path as API base path, if any.
				.pathMapping(contextPath)
				.apiInfo(apiInfo())
				// Configure the tags for grouping API endpoints.
				.tags(new Tag("MyIdentity",
								"API allowing queries of the user identity for the authenticated user"),
						new Tag("UserSkills",
								"API allowing queries and modifications of relationships from users to skills"),
						new Tag("SkillUsers",
								"API allowing queries and modifications of relationships from skills to users"),
						new Tag("Users",
								"API allowing queries and modifications of users"),
						new Tag("Skills",
								"API allowing queries and modifications of skills"),
						new Tag("Statistics",
								"API allowing queries of various statistics"),
						new Tag("Reports",
								"API allowing queries and creation of reports"));
	}

	@Bean
	public UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder()
				.deepLinking(true)
				.displayOperationId(false)
				.defaultModelsExpandDepth(1)
				.defaultModelExpandDepth(1)
				.defaultModelRendering(ModelRendering.EXAMPLE)
				.displayRequestDuration(false)
				.docExpansion(DocExpansion.NONE)
				.filter(false)
				.maxDisplayedTags(null)
				.operationsSorter(OperationsSorter.ALPHA)
				.showExtensions(false)
				.tagsSorter(TagsSorter.ALPHA)
				.supportedSubmitMethods(DEFAULT_SUBMIT_METHODS)
				.validatorUrl(null)
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(
				"MySkills API",
				"API of the MySkills server application",
				"1.0.0",
				"Terms of service",
				new Contact("Georg Wittberger", "https://georgwittberger.github.io/", "georg.wittberger@gmail.com"),
				"MIT", "https://opensource.org/licenses/MIT", emptyList());
	}
}
