//package io.knowledgeassets.myskills.server.report;
//
//import io.knowledgeassets.myskills.server.MySkillsServerApplicationTests;
//import io.knowledgeassets.myskills.server.common.Neo4jSessionFactoryConfiguration;
//import io.knowledgeassets.myskills.server.exception.BusinessException;
//import io.knowledgeassets.myskills.server.report.userskillpriority.UserSkillPriorityAggregationReport;
//import io.knowledgeassets.myskills.server.report.userskillpriorityaggregationreport.query.UserSkillPriorityAggregationReportQueryService;
//import io.knowledgeassets.myskills.server.report.userskillpriority.UserSkillPriorityReport;
//import io.knowledgeassets.myskills.server.report.userskillpriority.UserSkillPriorityReportRepository;
//import io.knowledgeassets.myskills.server.report.userskillpriority.UserSkillPriorityReportResponse;
//import io.knowledgeassets.myskills.server.report.userskillpriority.command.UserSkillPriorityReportCommandService;
//import io.knowledgeassets.myskills.server.report.userskillpriority.query.UserSkillPriorityReportQueryService;
//import io.knowledgeassets.myskills.server.report.userskill.UserSkillReport;
//import io.knowledgeassets.myskills.server.skill.Skill;
//import io.knowledgeassets.myskills.server.skill.SkillRepository;
//import SkillQueryController;
//import io.knowledgeassets.myskills.server.user.UserRepository;
//
//import io.knowledgeassets.myskills.server.userskill.UserSkillRepository;
//import org.apache.commons.codec.binary.Base64;
//import org.hamcrest.MatcherAssert;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.json.JacksonJsonParser;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.core.IsNull.notNullValue;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.springframework.http.HttpStatus.CREATED;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * Integration test for report
// */
//@AutoConfigureMockMvc
//public class UserSkillPriorityReportIntegrationTests extends MySkillsServerApplicationTests {
//
//	private static final String REPORT_RESOURCE_URL = "/reports";
//
//	@Autowired
//	private WebApplicationContext wac;
//	@Autowired
//	private MockMvc mockMvc;
//	@Autowired
//	private TestRestTemplate restTemplate;
//
//	private String userSkillPriorityReportId;
//
//	@Autowired
//	private UserRepository userRepository;
//	@Autowired
//	private SkillRepository skillRepository;
//	@Autowired
//	private UserSkillRepository userSkillRepository;
//	@Autowired
//	private UserSkillPriorityReportRepository userSkillPriorityReportRepository;
//	@Autowired
//	private UserSkillPriorityReportQueryService userSkillPriorityReportQueryService;
//	@Autowired
//	private UserSkillPriorityReportCommandService userSkillPriorityReportCommandService;
//	@Autowired
//	private UserSkillPriorityAggregationReportQueryService userSkillPriorityAggregationReportQueryService;
//	@Autowired
//	private InitializeDataForReport initializeDataForReport;
//
//	@BeforeEach
//	void init() {
//		initializeDataForReport.createData();
//	}
//
//	@Test
//	public void createAndReadReport() throws Exception {
//		createTable();
//	}
//
//	private void createTable() throws Exception {
//		String accessToken = obtainAccessToken("dummyUser", "dummyPassword");
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Authorization", "Bearer " + accessToken);
//		HttpEntity<Object> request = new HttpEntity<>(null, headers);
//
//		final ResponseEntity<UserSkillPriorityReportResponse> response =
//				restTemplate.postForEntity(REPORT_RESOURCE_URL, request, UserSkillPriorityReportResponse.class);
//
//		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//		UserSkillPriorityReportResponse report = response.getBody();
//		assertThat(report).isNotNull();
//		assertThat(report.getId()).isNotNull();
//		assertThat(report.getDate().truncatedTo(ChronoUnit.MINUTES))
//				.isEqualTo(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
//		assertThat(report.getSkillCount()).isEqualTo(4);
//	}
//
//	private String obtainAccessToken(String username, String password) throws Exception {
//
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//		params.add("grant_type", "password");
//		params.add("username", username);
//		params.add("password", password);
//		params.add("scope", "openid");
//
////		String base64ClientCredentials = new String(Base64.getEncoder().encode("user:password".getBytes()));
//		String base64ClientCredentials = new String(Base64.encodeBase64("user:password".getBytes()));
//
//		ResultActions result
//				= mockMvc.perform(post("/oauth/token")
//				.params(params)
//				.header("Authorization", "Basic " + base64ClientCredentials)
//				.accept("application/json;charset=UTF-8"))
//				.andExpect(status().isOk());
//
//		String resultString = result.andReturn().getResponse().getContentAsString();
//
//		JacksonJsonParser jsonParser = new JacksonJsonParser();
//		return jsonParser.parseMap(resultString).get("access_token").toString();
//	}
//
//}
//
//
//
//
//
//
////		assertEquals(2, userRepository.count());
////		assertEquals(4, skillRepository.count());
////		assertEquals(7, userSkillRepository.count());
////
////		assertEquals(1, userSkillPriorityReportRepository.count());
////
////		List<UserSkillPriorityAggregationReport> aggregationReports = null;
////		try {
////			aggregationReports = userSkillPriorityAggregationReportQueryService
////					.getUserSkillPriorityAggregationReportsByReportId(report.getId())
////					.collect(Collectors.toList());
////		} catch (BusinessException e) {
////			fail(e);
////		}
////
////		assertEquals(3, aggregationReports.size());
////		UserSkillPriorityAggregationReport angular = aggregationReports.stream()
////				.filter(userSkillPriorityAggregationReport ->
////						userSkillPriorityAggregationReport.getSkillName().equals("Angular")
////				).collect(Collectors.toList()).get(0);
////
////		assertThat(angular.getAveragePriority()).isEqualTo(1.5);
////		assertThat(angular.getMaximumPriority()).isEqualTo(2);
////		assertThat(angular.getUserCount()).isEqualTo(2);
////
////		List<UserSkillReport> userSkillReports = angular.getUserSkillReports();
////		UserSkillReport tester2 = userSkillReports.stream()
////				.filter(userSkillReport ->
////						userSkillReport.getUserName().equals("tester2")
////				).collect(Collectors.toList()).get(0);
////		;
////
////		assertThat(tester2.getCurrentLevel()).isEqualTo(2);
////		assertThat(tester2.getDesiredLevel()).isEqualTo(4);
////		assertThat(tester2.getPriority()).isEqualTo(1);
