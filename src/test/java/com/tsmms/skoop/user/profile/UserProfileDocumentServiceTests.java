package com.tsmms.skoop.user.profile;

import com.tsmms.skoop.skill.Skill;
import com.tsmms.skoop.user.User;
import com.tsmms.skoop.user.UserRepository;
import com.tsmms.skoop.userskill.UserSkill;
import com.tsmms.skoop.userskill.UserSkillRepository;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserProfileDocumentServiceTests {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserSkillRepository userSkillRepository;

	@Mock
	private UserProfileDocumentTemplateReader userProfileDocumentTemplateReader;

	private UserProfileDocumentService userProfileDocumentService;

	@Test
	@DisplayName("Tests if user profile document is built")
	void testIfUserProfileDocumentIsBuilt() throws IOException {

		try (InputStream templateInputStream = new ClassPathResource("templates/user-profile.docx").getInputStream();
			 XWPFDocument d = new XWPFDocument(templateInputStream)) {

			given(userProfileDocumentTemplateReader.getTemplate()).willReturn(d);

			this.userProfileDocumentService = new UserProfileDocumentService(userRepository, userSkillRepository, userProfileDocumentTemplateReader);

			User user = User.builder()
					.id("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")
					.referenceId("5acc24df-792a-4458-8d01-0c67033eceff")
					.userName("johndoe")
					.firstName("John")
					.lastName("Doe")
					.email("john.doe@mail.com")
					.coach(false)
					.academicDegree("Diplom-Wirtschaftsinformatiker")
					.positionProfile("Software Developer")
					.summary("Developer")
					.industrySectors(Arrays.asList("Automotive", "Telecommunication"))
					.specializations(Arrays.asList("IT Consulting", "Software Integration"))
					.certificates(Collections.singletonList("Java Certified Programmer"))
					.languages(Collections.singletonList("Deutsch"))
					.build();

			given(userRepository.findByReferenceId("5acc24df-792a-4458-8d01-0c67033eceff")).willReturn(
					Optional.of(user)
			);

			given(userSkillRepository.findByUserIdOrderByCurrentLevelDesc("adac977c-8e0d-4e00-98a8-da7b44aa5dd6"))
					.willReturn(Arrays.asList(
							UserSkill.builder()
									.id(1L)
									.user(user)
									.skill(Skill.builder()
											.id("e441613b-319f-4698-917d-6a4037c8e330")
											.name("Angular")
											.description("JavaScript Framework")
											.build())
									.currentLevel(2)
									.desiredLevel(3)
									.priority(4)
									.build(),
							UserSkill.builder()
									.id(2L)
									.user(user)
									.skill(Skill.builder()
											.id("3d4236c9-d84a-420a-baee-27b263118a28")
											.name("Spring Boot")
											.description("Java Framework")
											.build())
									.currentLevel(1)
									.desiredLevel(2)
									.priority(3)
									.build()
					));

			final byte[] anonymousUserProfileDocument = userProfileDocumentService.getAnonymousUserProfileDocument("5acc24df-792a-4458-8d01-0c67033eceff");
			assertThat(anonymousUserProfileDocument).isNotEmpty();
			try (InputStream is = new ByteArrayInputStream(anonymousUserProfileDocument)) {
				assertThat(is).isNotNull();
				XWPFDocument document = new XWPFDocument(is);
				assertThat(document).isNotNull();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, "Spring Boot")).isTrue();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, "Angular")).isTrue();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, "Software Developer")).isTrue();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, "Developer")).isTrue();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, "Diplom-Wirtschaftsinformatiker")).isTrue();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, "Automotive")).isTrue();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, "Telecommunication")).isTrue();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, "IT Consulting")).isTrue();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, "Software Integration")).isTrue();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, "Java Certified Programmer")).isTrue();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, "Deutsch")).isTrue();
			}
		}
	}

	@Test
	@DisplayName("Tests if user profile document is built when the optional fields are nulls.")
	void testIfUserProfileDocumentIsBuiltWhenOptionalFieldsAreNulls() throws IOException {

		try (InputStream templateInputStream = new ClassPathResource("templates/user-profile.docx").getInputStream();
			 XWPFDocument d = new XWPFDocument(templateInputStream)) {

			given(userProfileDocumentTemplateReader.getTemplate()).willReturn(d);

			this.userProfileDocumentService = new UserProfileDocumentService(userRepository, userSkillRepository, userProfileDocumentTemplateReader);

			User user = User.builder()
					.id("adac977c-8e0d-4e00-98a8-da7b44aa5dd6")
					.referenceId("5acc24df-792a-4458-8d01-0c67033eceff")
					.userName("johndoe")
					.firstName("John")
					.lastName("Doe")
					.email("john.doe@mail.com")
					.coach(false)
					.build();

			given(userRepository.findByReferenceId("5acc24df-792a-4458-8d01-0c67033eceff")).willReturn(
					Optional.of(user)
			);

			given(userSkillRepository.findByUserIdOrderByCurrentLevelDesc("adac977c-8e0d-4e00-98a8-da7b44aa5dd6"))
					.willReturn(Arrays.asList(
							UserSkill.builder()
									.id(1L)
									.user(user)
									.skill(Skill.builder()
											.id("e441613b-319f-4698-917d-6a4037c8e330")
											.name("Angular")
											.description("JavaScript Framework")
											.build())
									.currentLevel(2)
									.desiredLevel(3)
									.priority(4)
									.build(),
							UserSkill.builder()
									.id(2L)
									.user(user)
									.skill(Skill.builder()
											.id("3d4236c9-d84a-420a-baee-27b263118a28")
											.name("Spring Boot")
											.description("Java Framework")
											.build())
									.currentLevel(1)
									.desiredLevel(2)
									.priority(3)
									.build()
					));

			final byte[] anonymousUserProfileDocument = userProfileDocumentService.getAnonymousUserProfileDocument("5acc24df-792a-4458-8d01-0c67033eceff");
			assertThat(anonymousUserProfileDocument).isNotEmpty();
			try (InputStream is = new ByteArrayInputStream(anonymousUserProfileDocument)) {
				assertThat(is).isNotNull();
				XWPFDocument document = new XWPFDocument(is);
				assertThat(document).isNotNull();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, "Spring Boot")).isTrue();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, "Angular")).isTrue();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, UserProfileDocumentService.UserProfilePlaceholder.POSITION_PROFILE.getName())).isFalse();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, UserProfileDocumentService.UserProfilePlaceholder.ACADEMIC_DEGREE.getName())).isFalse();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, UserProfileDocumentService.UserProfilePlaceholder.SPECIALIZATIONS.getName())).isFalse();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, UserProfileDocumentService.UserProfilePlaceholder.LANGUAGES.getName())).isFalse();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, UserProfileDocumentService.UserProfilePlaceholder.INDUSTRY_SECTORS.getName())).isFalse();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, UserProfileDocumentService.UserProfilePlaceholder.CERTIFICATES.getName())).isFalse();
				assertThat(UserProfileDocumentTestUtils.checkIfDocumentContainsText(document, UserProfileDocumentService.UserProfilePlaceholder.SKILLS.getName())).isFalse();
			}
		}
	}

}
