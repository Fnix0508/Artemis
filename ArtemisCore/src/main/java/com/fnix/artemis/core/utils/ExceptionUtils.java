package com.fnix.artemis.core.utils;

public class ExceptionUtils {

    private static final String USERNAME_EXIST = "username.exist";

    private static final String MATCH_ALREADY_FINISHED = "match.already.finished";

    private static final String MATCH_NOT_TURN = "match.not.turn";

    public static RuntimeException usernameExistException() { return new RuntimeException(USERNAME_EXIST); }

    public static RuntimeException matchAlreadyFinishedException() { return new RuntimeException(MATCH_ALREADY_FINISHED); }

    public static RuntimeException matchNotTurnException() { return new RuntimeException(MATCH_NOT_TURN); }
}
