package com.tsmms.skoop.user;

public enum UserPermissionScope {
	/**
	 * Allows authorized users to view relationships between the owning user and her/his skills. For example, user Bob
	 * can view the skill profile of user John.
	 */
	READ_USER_SKILLS,
	/**
	 * Allows authorized users to view profile of the owning user and her / his personal information. For example, user Bob
	 * can view the profile and personal information of user John.
	 */
	READ_USER_PROFILE,
	/**
	 * Allows authorized users to see the owning user as a coach. For example, user Bob
	 * can see user John as a coach.
	 */
	SEE_AS_COACH
}
