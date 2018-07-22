package com.fnix.artemis.core.utils;

import java.util.Collection;
import java.util.concurrent.Future;

public class ThreadUtils {

    public static void waitAllDone(Collection<Future> futures) {
        try {
            for (Future future : futures) {
                future.get();
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
