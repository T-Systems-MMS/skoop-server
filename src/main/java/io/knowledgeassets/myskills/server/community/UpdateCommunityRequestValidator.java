package io.knowledgeassets.myskills.server.community;

import io.knowledgeassets.myskills.server.security.CurrentUserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UpdateCommunityRequestValidator implements ConstraintValidator<ValidUpdateCommunityRequest, CommunityRequest> {

	private final CurrentUserService currentUserService;

	public UpdateCommunityRequestValidator(CurrentUserService currentUserService) {
		this.currentUserService = currentUserService;
	}

	@Override
	public boolean isValid(CommunityRequest request, ConstraintValidatorContext context) {
		final String userId = currentUserService.getCurrentUser().getId();
		return checkIfCommunityManagerDoesNotTryToChangeHisOwnRole(request, context, userId);
	}

	/**
	 * Checks if a community manager does not try to change his own role.
	 * @param request - community change request
	 * @param context - constraint validator context
	 * @param userId - user id
	 * @return {@code true} in case change request is valid, {@code false} otherwise
	 */
	private boolean checkIfCommunityManagerDoesNotTryToChangeHisOwnRole(CommunityRequest request, ConstraintValidatorContext context, String userId) {
		boolean isValid = true;
		if (request.getManagerIds() == null) {
			context.buildConstraintViolationWithTemplate("The community manager list cannot be empty. There must be at least one manager of the community.").addConstraintViolation();
			isValid = false;
		}
		else if (!request.getManagerIds().contains(userId)) {
			context.buildConstraintViolationWithTemplate("The community manager cannot change her/his own role.").addConstraintViolation();
			isValid = false;
		}
		if (request.getMemberIds() == null) {
			context.buildConstraintViolationWithTemplate("The community member list cannot be empty. There must be at least one member (community manager) of the community.").addConstraintViolation();
			isValid = false;
		}
		else if (!request.getMemberIds().contains(userId)) {
			context.buildConstraintViolationWithTemplate("The community manager cannot remove herself/himself from the list of members.").addConstraintViolation();
			isValid = false;
		}
		return isValid;
	}
}
