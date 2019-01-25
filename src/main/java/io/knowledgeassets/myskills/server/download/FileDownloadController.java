package io.knowledgeassets.myskills.server.download;

import io.knowledgeassets.myskills.server.exception.UserProfileDocumentException;
import io.knowledgeassets.myskills.server.user.profile.UserProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.poi.util.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Api(tags = "Download", description = "API to provide files for downloading.")
@RestController
@RequestMapping(path = "/download")
public class FileDownloadController {

	private final UserProfileService userProfileService;

	public FileDownloadController(UserProfileService userProfileService) {
		this.userProfileService = requireNonNull(userProfileService);
	}

	@ApiOperation(
			value = "Get anonymous user profile as MS Word (DOCX) file.",
			notes = "Get anonymous user profile as MS Word (DOCX) file."
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@GetMapping(path = "/users/{referenceId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<byte[]> getAnonymousUserProfileDocument(@PathVariable("referenceId") String referenceId) {
		try (InputStream is = userProfileService.getAnonymousUserProfileDocument(referenceId)) {
			final HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Content-Disposition", "attachment; filename=user-profile.docx");
			return new ResponseEntity<>(IOUtils.toByteArray(is), httpHeaders, HttpStatus.OK);
		}
		catch (IOException e) {
			throw new UserProfileDocumentException(format("An error has occurred when serving user " +
					"profile document for a user with the reference id %s", referenceId), e);
		}
	}

}
