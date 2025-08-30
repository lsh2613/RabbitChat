package com.rabbitmqprac.domain.context.user.service;


import com.rabbitmqprac.application.dto.user.res.UserDetailRes;
import com.rabbitmqprac.application.dto.user.req.NicknameCheckReq;
import com.rabbitmqprac.application.dto.user.req.NicknameUpdateReq;
import com.rabbitmqprac.application.mapper.UserMapper;
import com.rabbitmqprac.domain.context.user.dto.req.UserCreateReq;
import com.rabbitmqprac.domain.context.user.exception.UserErrorCode;
import com.rabbitmqprac.domain.context.user.exception.UserErrorException;
import com.rabbitmqprac.domain.persistence.user.entity.Role;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.domain.persistence.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User saveUserWithEncryptedPassword(UserCreateReq req) {
        if (existsUserByUsername(req.username()))
            throw new UserErrorException(UserErrorCode.CONFLICT_USERNAME);

        return saveUser(req);
    }

    private boolean existsUserByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private User saveUser(UserCreateReq req) {
        User user = req.toEntity();
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User readUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserErrorException(UserErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<UserDetailRes> getUserDetails() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toDetailRes)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDetailRes getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserErrorException(UserErrorCode.NOT_FOUND));
        return UserMapper.toDetailRes(user);
    }

    @Transactional(readOnly = true)
    public User readUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserErrorException(UserErrorCode.NOT_FOUND));
    }

    @Transactional
    public void updateNickname(Long userId, NicknameUpdateReq req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserErrorException(UserErrorCode.NOT_FOUND));

        validateNicknameDuplication(req.nickname());

        user.updateNickname(req.nickname());
    }

    @Transactional(readOnly = true)
    public void validateNicknameDuplication(String nickname) {
        if (userRepository.existsByNickname(nickname))
            throw new UserErrorException(UserErrorCode.CONFLICT_USERNAME);
    }

    @Transactional(readOnly = true)
    public Boolean isDuplicatedUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public User create(String nickname) {
        User user = User.of(nickname, Role.USER);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicatedNickname(NicknameCheckReq req) {
        return userRepository.existsByNickname(req.nickname());
    }
}
