package com.tsmms.skoop.user.profile;

import com.tsmms.skoop.exception.UserProfileDocumentException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserProfileDocumentTemplateReaderTests {

	private UserProfileDocumentTemplateReader userProfileDocumentTemplateReader;

	@Test
	@DisplayName("Tests if user profile document template is retrieved falling back to the default template when a wrong path to a template is set.")
	void testIfUserProfileDocumentTemplateIsRetrievedFallingBackToDefaultTemplateWhenWrongPathToTemplateIsSet() throws Exception {
		this.userProfileDocumentTemplateReader = new UserProfileDocumentTemplateReader("some/not/existing/path");
		try (XWPFDocument document = this.userProfileDocumentTemplateReader.getTemplate()) {
			assertThat(document).isNotNull();;
		}
	}

	@Test
	@DisplayName("Tests if user profile document template is retrieved.")
	void testIfUserProfileDocumentTemplateIsRetrieved() throws Exception {
		this.userProfileDocumentTemplateReader = new UserProfileDocumentTemplateReader(new ClassPathResource("templates/user-profile-test-template.docx").getFile().getAbsolutePath());
		try (XWPFDocument document = this.userProfileDocumentTemplateReader.getTemplate()) {
			assertThat(document).isNotNull();
			assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, "Test template")).isTrue();
		}
	}

	@Test
	@DisplayName("Tests if an exception is thrown when invalid template is used.")
	void testIfExceptionIsThrownWhenInvalidTemplateIsUsed() throws IOException {
		this.userProfileDocumentTemplateReader = new UserProfileDocumentTemplateReader(new ClassPathResource("templates/fake-template.docx").getFile().getAbsolutePath());
		assertThrows(UserProfileDocumentException.class, () -> userProfileDocumentTemplateReader.getTemplate());
	}

}
