package com.fnix.artemis.core.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    public static final LocalDateTime now () {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
