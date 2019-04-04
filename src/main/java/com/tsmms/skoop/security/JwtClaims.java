package com.tsmms.skoop.security;

public final class JwtClaims {

	private JwtClaims() {
		throw new IllegalStateException("Utility class");
	}

	public static final String USER_NAME = "user_name";
	public static final String FIRST_NAME = "given_name";
	public static final String LAST_NAME = "family_name";
	public static final String EMAIL = "email";
	public static final String SKOOP_USER_ID = "skoop_user_id";
}
