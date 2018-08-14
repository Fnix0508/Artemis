package com.fnix.artemis.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fnix.artemis.core.model.User;
import com.fnix.artemis.core.repository.UserDao;
import com.google.common.collect.Lists;

@Service
public class LocalUserDetailService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public ArtemisUser loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return createFromUser(user);
    }

    private ArtemisUser createFromUser(User user) {
        return new ArtemisUser(user.getUsername(), user.getPassword(), user.getId(), user.getNick(), Lists.newArrayList());
    }
}
