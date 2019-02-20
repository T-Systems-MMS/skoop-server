package io.knowledgeassets.myskills.server.user.profile;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.IBody;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public final class UserProfileDocumentTestUtils {

	private UserProfileDocumentTestUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static boolean checkIfDocumentContainsText(XWPFDocument document, String text) {
		if (checkIfBodyContainsText(document, text)) {
			return true;
		} else {
			return checkIfTablesContainText(document, text);
		}
	}

	private static boolean checkIfBodyContainsText(IBody body, String text) {
		if (body.getParagraphs() != null) {
			return body.getParagraphs().stream().anyMatch(p -> {
				if (p.getRuns() != null) {
					return p.getRuns().stream().anyMatch(r -> StringUtils.containsIgnoreCase(r.getText(0), text));
				} else {
					return false;
				}
			});
		} else {
			return false;
		}
	}

	private static boolean checkIfTablesContainText(IBody body, String text) {
		if (body.getTables() != null) {
			return body.getTables().stream().anyMatch(t -> {
				if (t.getRows() != null) {
					return t.getRows().stream().anyMatch(r -> {
						if (r.getTableCells() != null) {
							return r.getTableCells().stream().anyMatch(c -> checkIfBodyContainsText(c, text));
						} else {
							return false;
						}
					});
				} else {
					return false;
				}
			});
		}
		else {
			return false;
		}
	}

}
