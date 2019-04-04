package com.tsmms.skoop.download;

import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.profile.UserProfileDocumentService;
import com.tsmms.skoop.common.AbstractControllerTests;
import com.tsmms.skoop.exception.UserProfileDocumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static com.tsmms.skoop.common.JwtAuthenticationFactory.withUser;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserProfileDownloadController.class)
class UserProfileDownloadControllerTests extends AbstractControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserProfileDocumentService userProfileDocumentService;

	@Test
	@DisplayName("Tests if user profile file can be downloaded.")
	void testIfUserProfileFileCanBeDownloaded() throws Exception {
		try (final InputStream is = new ClassPathResource("templates/user-profile.docx").getInputStream();
			 final InputStream expectedContent = new ClassPathResource("templates/user-profile.docx").getInputStream()) {
			given(userProfileDocumentService.getAnonymousUserProfileDocument("5acc24df-792a-4458-8d01-0c67033eceff")).willReturn(is.readAllBytes());

			final User owner = User.builder()
					.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
					.userName("tester")
					.build();

			mockMvc.perform(get("/download/users/5acc24df-792a-4458-8d01-0c67033eceff")
					.with(authentication(withUser(owner))))
					.andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM))
					.andExpect(content().bytes(expectedContent.readAllBytes()))
					.andExpect(header().string("Content-Disposition","attachment; filename=user-profile.docx"));
		}
	}

	@Test
	@DisplayName("Tests if UNAUTHORIZED status code is returned when not authenticated user downloads user profile.")
	void testIfUnauthorizedStatusCodeIsReturnedWhenNotAuthenticatedUserDownloadsUserProfile() throws Exception {
		mockMvc.perform(get("/download/users/5acc24df-792a-4458-8d01-0c67033eceff"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("Tests if INTERNAL_SERVER_ERROR is thrown when there is an issue with getting a document.")
	void testIfInternalServerErrorIsThrownWhenThereIsAnIssueWithGettingOfDocument() throws Exception {
		given(userProfileDocumentService.getAnonymousUserProfileDocument("5acc24df-792a-4458-8d01-0c67033eceff"))
				.willThrow(new UserProfileDocumentException("An error has occurred when getting a document"));

		final User owner = User.builder()
				.id("1f37fb2a-b4d0-4119-9113-4677beb20ae2")
				.userName("tester")
				.build();

		mockMvc.perform(get("/download/users/5acc24df-792a-4458-8d01-0c67033eceff")
				.with(authentication(withUser(owner))))
				.andExpect(status().isInternalServerError());
	}

}
