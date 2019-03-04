package io.knowledgeassets.myskills.server.communityuser.registration.command;

import io.knowledgeassets.myskills.server.common.AbstractControllerTests;
import io.knowledgeassets.myskills.server.community.Community;
import io.knowledgeassets.myskills.server.community.query.CommunityQueryService;
import io.knowledgeassets.myskills.server.communityuser.registration.CommunityUserRegistration;
import io.knowledgeassets.myskills.server.communityuser.registration.query.CommunityUserRegistrationQueryService;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.query.UserQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static io.knowledgeassets.myskills.server.common.JwtAuthenticationFactory.withUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunityUserRegistrationCommandController.class)
class CommunityUserRegistrationCommandControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CommunityUserRegistrationCommandService communityUserRegistrationCommandService;

	@MockBean
	private UserQueryService userQueryService;

	@MockBean
	private CommunityQueryService communityQueryService;

	@MockBean
	private CommunityUserRegistrationQueryService communityUserRegistrationQueryService;

	@DisplayName("Create user registration on behalf of community manager.")
	@Test
	void createUserRegistrationsOnBehalfOfCommunity() throws Exception {

		final User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(userQueryService.getUsersByIds(Arrays.asList("abc", "def"))).willReturn(Stream.of(User.builder()
						.id("abc")
						.userName("firstUser")
						.build(),
				User.builder()
						.id("def")
						.userName("secondUser")
						.build()
		));

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.build()
		));

		given(securityService.isCommunityManager("123")).willReturn(true);

		given(communityUserRegistrationCommandService.createUserRegistrationsOnBehalfOfCommunity(Arrays.asList(
				User.builder()
						.id("abc")
						.userName("firstUser")
						.build(),
				User.builder()
						.id("def")
						.userName("secondUser")
						.build()
		), Community.builder()
				.id("123")
				.title("Java User Group")
				.build())).willReturn(Arrays.asList(CommunityUserRegistration.builder()
						.registeredUser(User.builder()
								.id("abc")
								.userName("firstUser")
								.build())
						.approvedByCommunity(true)
						.approvedByUser(false)
						.build(),
				CommunityUserRegistration.builder()
						.registeredUser(User.builder()
								.id("def")
								.userName("secondUser")
								.build())
						.approvedByCommunity(true)
						.approvedByUser(false)
						.build()
		));

		final ClassPathResource body = new ClassPathResource("communityuser/registration/command/create-user-registrations.json");

		try (InputStream is = body.getInputStream()) {

			mockMvc.perform(post("/communities/123/user-registrations").content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$[0].user.userName", is(equalTo("firstUser"))))
					.andExpect(jsonPath("$[0].approvedByUser", is(equalTo(false))))
					.andExpect(jsonPath("$[0].approvedByCommunity", is(equalTo(true))))
					.andExpect(jsonPath("$[1].user.userName", is(equalTo("secondUser"))))
					.andExpect(jsonPath("$[1].approvedByUser", is(equalTo(false))))
					.andExpect(jsonPath("$[1].approvedByCommunity", is(equalTo(true))));
		}
	}

	@DisplayName("Create user registration on behalf of user.")
	@Test
	void createUserRegistrationOnBehalfOfUser() throws Exception {

		final User tester = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		given(securityService.isAuthenticatedUserId("1f37fb2a-b4d0-4119-9113-4677beb20ae2")).willReturn(true);

		given(userQueryService.getUsersByIds(Collections.singletonList("1f37fb2a-b4d0-4119-9113-4677beb20ae2"))).willReturn(Stream.of(User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build()
		));

		given(communityQueryService.getCommunityById("123")).willReturn(Optional.of(
				Community.builder()
						.id("123")
						.title("Java User Group")
						.build()
		));

		given(communityUserRegistrationCommandService.createUserRegistrationsOnBehalfOfCommunity(Collections.singletonList(
				User.builder()
						.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
						.userName("tester")
						.build()
		), Community.builder()
				.id("123")
				.title("Java User Group")
				.build())
		).willReturn(Collections.singletonList(CommunityUserRegistration.builder()
						.registeredUser(User.builder()
								.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
								.userName("tester")
								.build())
						.approvedByCommunity(false)
						.approvedByUser(true)
						.build()
		));

		final ClassPathResource body = new ClassPathResource("communityuser/registration/command/create-user-registration.json");

		try (InputStream is = body.getInputStream()) {

			mockMvc.perform(post("/communities/123/user-registrations").content(is.readAllBytes())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.with(authentication(withUser(tester))))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$[0].user.userName", is(equalTo("tester"))))
					.andExpect(jsonPath("$[0].approvedByUser", is(equalTo(true))))
					.andExpect(jsonPath("$[0].approvedByCommunity", is(equalTo(false))));
		}

	}

}
