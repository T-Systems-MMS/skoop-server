package io.knowledgeassets.myskills.server.user.notification;

import io.knowledgeassets.myskills.server.notification.AbstractNotificationResponse;
import io.knowledgeassets.myskills.server.communityuser.registration.notification.CommunityUserRegistrationNotificationResponse;
import io.knowledgeassets.myskills.server.notification.Notification;
import io.knowledgeassets.myskills.server.notification.NotificationType;
import io.knowledgeassets.myskills.server.notification.query.NotificationQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Api(tags = "UserNotifications")
@RestController
public class UserNotificationQueryController {

	private static final Map<NotificationType, Function<Notification, AbstractNotificationResponse>> MAPPERS = new HashMap<>() {{
		put(NotificationType.REQUEST_TO_JOIN_COMMUNITY, CommunityUserRegistrationNotificationResponse::of);
		put(NotificationType.INVITATION_TO_JOIN_COMMUNITY, CommunityUserRegistrationNotificationResponse::of);
		put(NotificationType.ACCEPTANCE_TO_COMMUNITY, CommunityUserRegistrationNotificationResponse::of);
	}};

	private final NotificationQueryService notificationQueryService;

	public UserNotificationQueryController(NotificationQueryService notificationQueryService) {
		this.notificationQueryService = notificationQueryService;
	}

	@ApiOperation(
			value = "Gets all user notifications.",
			notes = "Gets all user notifications."
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
				.map(n -> MAPPERS.get(n.getType()).apply(n)).collect(Collectors.toList()));
	}

}
