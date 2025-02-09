package com.course.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants for Spring Security authorities.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthoritiesConstants {

    public static final String ANONYMOUS = "anonymousUser";

    public static final String ROLE_INSTRUCTOR = "INSTRUCTOR";

    public static final String ROLE_LEARNER = "LEARNER";

    public static final String ROLE_ADMIN = "ADMIN";
}
