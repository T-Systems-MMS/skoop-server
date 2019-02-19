package io.knowledgeassets.myskills.server.user.profile;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserProfileDocumentTemplateReaderTests {

	private UserProfileDocumentTemplateReader userProfileDocumentTemplateReader;

	@Test
	@DisplayName("Tests if user profile document template is retrieved falling back to the default template when a wrong path to a template is set.")
	void testIfUserProfileDocumentTemplateIsRetrievedFallingBackToDefaultTemplateWhenWrongPathToTemplateIsSet() throws Exception {
		this.userProfileDocumentTemplateReader = new UserProfileDocumentTemplateReader("some/not/existing/path");
		try (InputStream is = this.userProfileDocumentTemplateReader.getTemplate()) {
			assertThat(is).isNotNull();;
			final XWPFDocument document = new XWPFDocument(is);
			assertThat(document).isNotNull();
		}
	}

	@Test
	@DisplayName("Tests if user profile document template is retrieved.")
	void testIfUserProfileDocumentTemplateIsRetrieved() throws Exception {
		this.userProfileDocumentTemplateReader = new UserProfileDocumentTemplateReader(new ClassPathResource("templates/user-profile-test-template.docx").getFile().getAbsolutePath());
		try (InputStream is = this.userProfileDocumentTemplateReader.getTemplate()) {
			assertThat(is).isNotNull();;
			final XWPFDocument document = new XWPFDocument(is);
			assertThat(document).isNotNull();
			assertThat(StringUtils.containsIgnoreCase(document.getParagraphs().get(0).getRuns().get(0).getText(0), "Test template")).isTrue();
		}
	}

}
