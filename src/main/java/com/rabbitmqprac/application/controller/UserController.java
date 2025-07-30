package com.rabbitmqprac.application.controller;

import com.rabbitmqprac.application.dto.user.res.UserDetailRes;
import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import com.rabbitmqprac.domain.context.user.service.UserService;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/me")
    public UserDetailRes getMember(@AuthenticationPrincipal SecurityUserDetails user) {
        return userService.getUserDetail(user.getUserId());
    }

    @GetMapping("/users")
    public List<User> getMembers() {
        return userService.getUsers();
    }
}
