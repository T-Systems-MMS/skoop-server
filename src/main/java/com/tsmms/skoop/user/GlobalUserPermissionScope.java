package com.tsmms.skoop.user;

public enum GlobalUserPermissionScope {

	/**
	 * Allows all users to view relationships between the owning user and her/his skills.
	 */
	READ_USER_SKILLS,
	/**
	 * Allows all users to view profile of the owning user and her / his personal information.
	 */
	READ_USER_PROFILE,
	/**
	 * Allows all users to see the owning user as a coach.
	 */
	FIND_AS_COACH

}
