package io.knowledgeassets.myskills.server.user.profile;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;

@Component
@Slf4j
public class UserProfileDocumentTemplateReader {

	private static final ClassPathResource DEFAULT_TEMPLATE_RESOURCE = new ClassPathResource("templates/user-profile.docx");

	private final String templatePath;

	public UserProfileDocumentTemplateReader(@Value("${myskills.user-profile-download.template-path:#{null}}") String templatePath) {
		this.templatePath = templatePath;
	}

	public InputStream getTemplate() throws IOException {
		if (StringUtils.isEmpty(this.templatePath)) {
			return DEFAULT_TEMPLATE_RESOURCE.getInputStream();
		}
		else {
			final Path path = Paths.get(this.templatePath);
			if (path.toFile().exists()) {
				try {
					return new FileSystemResource(path).getInputStream();
				}
				catch (IOException ex) {
					log.warn(format("The template file \"%s\" cannot be read. The default template file will be used as a fallback option.", path), ex);
					return DEFAULT_TEMPLATE_RESOURCE.getInputStream();
				}
			}
			else {
				log.warn(format("The template file \"%s\" does not exist. The default template file will be used as a fallback option.", path));
				return DEFAULT_TEMPLATE_RESOURCE.getInputStream();
			}
		}
	}

}
