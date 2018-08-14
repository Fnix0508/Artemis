package com.fnix.artemis.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fnix.artemis.base.model.Position;
import com.fnix.artemis.core.dto.CurrentMatchDto;
import com.fnix.artemis.core.model.CurrentMatchOrder;
import com.fnix.artemis.core.service.UserCurrentMatchService;
import com.fnix.artemis.web.security.ArtemisUser;
import com.fnix.artemis.web.security.ArtemisWebContext;

@RestController
@RequestMapping("/service/userCurrentMatch")
public class UserCurrentMatchController {

    @Autowired
    private UserCurrentMatchService userCurrentMatchService;

    @GetMapping("/get")
    public CurrentMatchDto getCurrentMatch() {
        ArtemisUser user = ArtemisWebContext.getCurrentUser();
        return userCurrentMatchService.getCurrentMatch(user.getUserId());
    }

    @PostMapping("/create")
    public CurrentMatchDto create() {
        ArtemisUser user = ArtemisWebContext.getCurrentUser();
        return userCurrentMatchService.create(user.getUserId());
    }

    @GetMapping("/getNextOrder")
    public List<CurrentMatchOrder> getMatchOrder(long currentMatchId, int roundIndex) {
        ArtemisUser user = ArtemisWebContext.getCurrentUser();
        return userCurrentMatchService.getMatchOrder(user.getUserId(), currentMatchId, roundIndex);
    }

    @PostMapping("/play")
    public CurrentMatchDto play(@RequestBody Position position) {
        ArtemisUser user = ArtemisWebContext.getCurrentUser();
        return userCurrentMatchService.play(user.getUserId(), position);
    }
}
