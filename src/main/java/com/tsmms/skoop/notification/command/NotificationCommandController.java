package com.tsmms.skoop.notification.command;

import com.tsmms.skoop.exception.NoSuchResourceException;
import com.tsmms.skoop.exception.UserNotAuthorizedException;
import com.tsmms.skoop.exception.enums.Model;
import com.tsmms.skoop.notification.Notification;
import com.tsmms.skoop.notification.query.NotificationQueryService;
import com.tsmms.skoop.security.CurrentUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.requireNonNull;

@Api(tags = { "Notifications" })
@RestController
public class NotificationCommandController {

	private final NotificationCommandService notificationCommandService;
	private final NotificationQueryService notificationQueryService;
	private final CurrentUserService currentUserService;

	public NotificationCommandController(NotificationCommandService notificationCommandService,
										 NotificationQueryService notificationQueryService,
										 CurrentUserService currentUserService) {
		this.notificationCommandService = requireNonNull(notificationCommandService);
		this.notificationQueryService = requireNonNull(notificationQueryService);
		this.currentUserService = requireNonNull(currentUserService);
	}

	@ApiOperation(value = "Deletes notification.",
			notes = "User joins the community as member.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 400, message = "Invalid input data, e.g. missing mandatory data or community name exists"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to perform this operation"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@DeleteMapping(path = "/notifications/{notificationId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> delete(@PathVariable("notificationId") String notificationId) {
		if (notificationQueryService.getNotification(notificationId).isEmpty()) {
			final String[] searchParamsMap = {"id", notificationId};
			throw NoSuchResourceException.builder()
					.model(Model.USER)
					.searchParamsMap(searchParamsMap)
					.build();
		}
		final Notification notification = notificationQueryService.getUserNotifications(currentUserService.getCurrentUserId())
				.filter(n -> notificationId.equals(n.getId()))
				.findFirst()
				.orElseThrow(() -> new UserNotAuthorizedException(String.format("The user does not have permissions to delete the notification with ID %s", notificationId)));
		if (!notification.isToDo()) {
			notificationCommandService.delete(notification);
			return ResponseEntity.noContent().build();
		} else {
			throw new UserNotAuthorizedException("The user cannot delete to-dos.");
		}
	}

}
