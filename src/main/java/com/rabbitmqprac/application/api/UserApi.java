package com.rabbitmqprac.application.api;

import com.rabbitmqprac.application.dto.auth.res.UserDetailRes;
import com.rabbitmqprac.application.dto.user.req.NicknameCheckReq;
import com.rabbitmqprac.application.dto.user.req.NicknameUpdateReq;
import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Tag(name = "[유저 API]")
public interface UserApi {

    @Operation(summary = "내 정보 조회")
    UserDetailRes getMember(@AuthenticationPrincipal SecurityUserDetails user);

    @Operation(summary = "전체 회원 조회")
    List<UserDetailRes> getMembers();

    @Operation(summary = "유저 아이디 중복 확인")
    Map<String, Boolean> isDuplicatedUsername(@RequestParam @Validated String username);

    @Operation(summary = "닉네임 변경")
    ResponseEntity<Void> patchNickname(
            @AuthenticationPrincipal SecurityUserDetails user,
            @RequestBody NicknameUpdateReq nicknameUpdateReq

    );

    @Operation(summary = "닉네임 중복 확인")
    Map<String, Boolean> checkNicknameDuplication(@Validated NicknameCheckReq nicknameCheckReq);
}
