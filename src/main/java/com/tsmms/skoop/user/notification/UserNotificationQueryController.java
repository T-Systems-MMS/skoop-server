package com.tsmms.skoop.user.notification;

import com.tsmms.skoop.notification.AbstractNotificationResponse;
import com.tsmms.skoop.notification.query.NotificationQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Api(tags = "UserNotifications")
@RestController
public class UserNotificationQueryController {

	private final ConversionService conversionService;

	private final NotificationQueryService notificationQueryService;

	public UserNotificationQueryController(NotificationQueryService notificationQueryService,
										   ConversionService conversionService) {
		this.notificationQueryService = requireNonNull(notificationQueryService);
		this.conversionService = requireNonNull(conversionService);
	}

	@ApiOperation(
			value = "Gets all user notifications.",
			notes = "Gets all user notifications. A notification has type field indicating what kind of notification it is. See AbstractNotificationResponse documentation for details."
	)
	@ApiResponses({
			@ApiResponse(code = 200, message = "Successful execution"),
			@ApiResponse(code = 401, message = "Invalid authentication"),
			@ApiResponse(code = 403, message = "Insufficient privileges to access resource"),
			@ApiResponse(code = 500, message = "Error during execution")
	})
	@PreAuthorize("isAuthenticated() && isPrincipalUserId(#userId)")
	@GetMapping(path = "/users/{userId}/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AbstractNotificationResponse>> getUserNotifications(@PathVariable("userId") String userId) {
		return ResponseEntity.ok(notificationQueryService.getUserNotifications(userId)
				.map(n -> conversionService.convert(n, AbstractNotificationResponse.class)).collect(Collectors.toList()));
	}

}
