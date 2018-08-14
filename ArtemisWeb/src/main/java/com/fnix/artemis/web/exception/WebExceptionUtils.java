package com.fnix.artemis.web.exception;

public class WebExceptionUtils {

    public static final String USERNAME_EXIST = "username.exist";

    public static RuntimeException usernameExistException() {
        return new RuntimeException(USERNAME_EXIST);
    }
}
