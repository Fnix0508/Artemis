package com.fnix.artemis.web.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ArtemisUser extends User {

    public ArtemisUser(String username, String password, Long userId, String nick, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.nick = nick;
    }

    private Long userId;

    private String nick;

}
