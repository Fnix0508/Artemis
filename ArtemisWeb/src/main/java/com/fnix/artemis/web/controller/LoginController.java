package com.fnix.artemis.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fnix.artemis.core.model.User;
import com.fnix.artemis.web.security.ArtemisUser;
import com.fnix.artemis.web.security.ArtemisWebContext;
import com.fnix.artemis.web.service.UserWebService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserWebService userWebService;

    @PostMapping("/register")
    public void create(@RequestBody User user) {
        userWebService.create(user);
    }

    @RequestMapping("/login")
    public void login() {
        ArtemisUser user = ArtemisWebContext.getCurrentUser();
        LOG.info("用户登录！nick={}", user.getNick());
    }

}
