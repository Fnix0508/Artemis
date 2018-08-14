package com.fnix.artemis.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fnix.artemis.core.model.User;
import com.fnix.artemis.core.repository.UserDao;
import com.fnix.artemis.web.exception.WebExceptionUtils;

@Service
public class UserWebService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    public void create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User dbUser = userDao.findByUsername(user.getUsername());
        if (dbUser != null) {
            throw WebExceptionUtils.usernameExistException();
        }
        userDao.save(user);
    }
}
