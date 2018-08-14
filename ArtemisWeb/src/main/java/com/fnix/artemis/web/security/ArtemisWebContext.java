package com.fnix.artemis.web.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class ArtemisWebContext {

    public static ArtemisUser getCurrentUser() {
        return (ArtemisUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
