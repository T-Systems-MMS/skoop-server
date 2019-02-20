package io.knowledgeassets.myskills.server.user.profile;

import io.knowledgeassets.myskills.server.exception.NoSuchResourceException;
import io.knowledgeassets.myskills.server.exception.UserProfileDocumentException;
import io.knowledgeassets.myskills.server.exception.enums.Model;
import io.knowledgeassets.myskills.server.user.User;
import io.knowledgeassets.myskills.server.user.UserRepository;
import io.knowledgeassets.myskills.server.userskill.UserSkill;
import io.knowledgeassets.myskills.server.userskill.UserSkillRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.xwpf.usermodel.IBody;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Service
public class UserProfileDocumentService {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy");

	private final UserRepository userRepository;
	private final UserSkillRepository userSkillRepository;
	private final UserProfileDocumentTemplateReader userProfileDocumentTemplateReader;

	public UserProfileDocumentService(UserRepository userRepository,
									  UserSkillRepository userSkillRepository,
									  UserProfileDocumentTemplateReader userProfileDocumentTemplateReader) {
		this.userRepository = requireNonNull(userRepository);
		this.userSkillRepository = requireNonNull(userSkillRepository);
		this.userProfileDocumentTemplateReader = requireNonNull(userProfileDocumentTemplateReader);
	}

	/**
	 * Retrieves MS Word document (DOCX) with anonymous user profile data as array of bytes.
	 * @param referenceId user reference id
	 * @return MS Word document with anonymous user profile data as array of bytes
	 */
	public byte[] getAnonymousUserProfileDocument(String referenceId) {

		final User user = getUserByReferenceId(referenceId);

		try (final XWPFDocument document = userProfileDocumentTemplateReader.getTemplate()) {
			replacePlaceholders(document, user);
			final ByteArrayOutputStream b = new ByteArrayOutputStream();
			document.write(b);
			return b.toByteArray();
		}
		catch (IOException e) {
			throw new UserProfileDocumentException(format("An error has occurred when building user " +
					"profile document for a user with the reference id %s", referenceId), e);
		}
	}

	private User getUserByReferenceId(String referenceId) {
		return userRepository.findByReferenceId(referenceId).orElseThrow(() -> {
			final String[] searchParamsMap = {"referenceId", referenceId};
			return NoSuchResourceException.builder()
					.model(Model.USER)
					.searchParamsMap(searchParamsMap)
					.build();
		});
	}

	private void replacePlaceholders(XWPFDocument document, User user) {
		replaceTablesPlaceholders(document, user);
		replaceParagraphsPlaceholders(document, user, document.getParagraphs());
		if (document.getFooterList() != null) {
			document.getFooterList().forEach(footer -> replaceTablesPlaceholders(footer, user));
		}
	}

	private void replaceTablesPlaceholders(IBody body, User user) {
		if (body.getTables() != null) {
			body.getTables().forEach(table -> {
				if (table.getRows() != null) {
					table.getRows().forEach(row -> {
						if (row.getTableCells() != null) {
							row.getTableCells().forEach(cell ->
									replaceParagraphsPlaceholders(cell, user, cell.getParagraphs())
							);
						}
					});
				}
			});
		}
	}

	private void replaceParagraphsPlaceholders(IBody body, User user, List<XWPFParagraph> paragraphs) {
		if (paragraphs != null) {
			// copy paragraph list to address paragraphs as copies to avoid throwing of java.util.ConcurrentModificationException
			// when inserting new paragraphs
			final List<XWPFParagraph> paragraphList = new ArrayList<>(paragraphs);
			paragraphList.forEach(p -> replaceParagraphPlaceholders(p, user, body));
		}
	}

	private void replaceParagraphPlaceholders(XWPFParagraph p, User user, IBody body) {
		if (p.getRuns() != null) {
			p.getRuns().forEach(r -> {
						replacePlaceholder(r, UserProfilePlaceholder.DATE.getName(), () -> SDF.format(new Date()));
						replacePlaceholder(r, UserProfilePlaceholder.ACADEMIC_DEGREE.getName(), user::getAcademicDegree);
						replacePlaceholder(r, UserProfilePlaceholder.POSITION_PROFILE.getName(), user::getPositionProfile);
						replaceListPlaceholder(r, UserProfilePlaceholder.SPECIALIZATIONS.getName(), user::getSpecializations, p, body);
						replaceListPlaceholder(r, UserProfilePlaceholder.LANGUAGES.getName(), user::getLanguages, p, body);
						replaceListPlaceholder(r, UserProfilePlaceholder.INDUSTRY_SECTORS.getName(), user::getIndustrySectors, p, body);
						replaceListPlaceholder(r, UserProfilePlaceholder.CERTIFICATES.getName(), user::getCertificates, p, body);
						final Supplier<List<String>> skillsSupplier = () ->
								StreamSupport.stream(userSkillRepository.findByUserIdOrderByCurrentLevelDesc(user.getId()).spliterator(), false)
										.map((UserSkill userSkill) -> {
											StringBuilder level = new StringBuilder();
											IntStream.rangeClosed(1, userSkill.getCurrentLevel()).forEach(a -> level.append("*"));
											return format("%s %s", userSkill.getSkill().getName(), level.toString());
										}).collect(Collectors.toList());
						replaceListPlaceholder(r, UserProfilePlaceholder.SKILLS.getName(), skillsSupplier, p, body);
					}
			);
		}
	}

	private static void replacePlaceholder(XWPFRun r, String placeholder, Supplier<String> valueSupplier) {
		final String text = r.getText(0);
		if (StringUtils.containsIgnoreCase(text, placeholder)) {
			final String value = valueSupplier.get();
			final String newText = value == null ? StringUtils.replaceIgnoreCase(text, placeholder, "N/A") :
					StringUtils.replaceIgnoreCase(text, placeholder, value);
			r.setText(newText, 0);
		}
	}

	private static void replaceListPlaceholder(XWPFRun r, String placeholder, Supplier<List<String>> valuesSupplier, XWPFParagraph paragraph, IBody body) {
		final String text = r.getText(0);
		if (StringUtils.containsIgnoreCase(text, placeholder)) {
			final List<String> values = valuesSupplier.get();
			if (values == null || values.isEmpty()) {
				final String newText = StringUtils.replaceIgnoreCase(text, placeholder, "N/A");
				// remove bullet styling
				paragraph.setNumID(BigInteger.ZERO);
				r.setText(newText, 0);
			}
			else {
				// get cursor to point a position a new paragraph should be inserted at
				XmlCursor cursor = paragraph.getCTP().newCursor();
				// the first paragraph in the list should have a bullet
				// so we can get its style and apply it to all other paragraphs in the list
				final CTPPr ctpPr = paragraph.getCTP().getPPr();
				for (int i = 0; i < values.size(); i++) {
					final String value = values.get(i);
					if (i == 0) {
						r.setText(value, 0);
					}
					else {
						cursor = appendNewListParagraph(body, cursor, value, ctpPr);
					}
				}
			}
		}
	}

	private static XmlCursor appendNewListParagraph(IBody body, XmlCursor cursor, String value, CTPPr ctpPr) {
		final XWPFParagraph p = body.insertNewParagraph(cursor);
		p.getCTP().setPPr(ctpPr);
		// change cursor to point a new position paragraph should be inserted at
		cursor = p.getCTP().newCursor();
		final XWPFRun r = p.createRun();
		r.setText(value);
		return cursor;
	}

	public enum UserProfilePlaceholder {

		DATE("${date}"),
		ACADEMIC_DEGREE("${academicdegree}"),
		POSITION_PROFILE("${positionprofile}"),
		SPECIALIZATIONS("${specializations}"),
		LANGUAGES("${languages}"),
		INDUSTRY_SECTORS("${industrysectors}"),
		CERTIFICATES("${certificates}"),
		SKILLS("${skills}");

		private final String name;

		UserProfilePlaceholder(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

}
