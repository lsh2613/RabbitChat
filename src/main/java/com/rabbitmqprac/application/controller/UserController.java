package com.rabbitmqprac.application.controller;

import com.rabbitmqprac.application.dto.auth.res.UserDetailRes;
import com.rabbitmqprac.application.dto.user.req.NicknameCheckReq;
import com.rabbitmqprac.application.dto.user.req.NicknameUpdateReq;
import com.rabbitmqprac.domain.context.user.service.UserService;
import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
    public List<UserDetailRes> getMembers() {
        return userService.getUserDetails();
    }

    @GetMapping("/users/username")
    public Map<String, Boolean> isDuplicatedUsername(@RequestParam @Validated String username) {
        return Map.of("isDuplicated", userService.isDuplicatedUsername(username));
    }

    @PatchMapping("/users/nickname")
    public ResponseEntity<Void> patchNickname(@AuthenticationPrincipal SecurityUserDetails user,
                                              @RequestBody NicknameUpdateReq nicknameUpdateReq) {
        userService.updateNickname(user.getUserId(), nicknameUpdateReq);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/nickname")
    public Map<String, Boolean> checkNicknameDuplication(@Validated NicknameCheckReq nicknameCheckReq) {
        return Map.of("isDuplicated", userService.isDuplicatedNickname(nicknameCheckReq));
    }
}
