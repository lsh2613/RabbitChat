package com.rabbitmqprac.domain.context.user.service;


import com.rabbitmqprac.application.dto.user.res.UserDetailRes;
import com.rabbitmqprac.application.mapper.UserMapper;
import com.rabbitmqprac.domain.context.user.exception.UserErrorCode;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.domain.persistence.user.repository.UserRepository;
import com.rabbitmqprac.global.exception.GlobalErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User readUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GlobalErrorException(UserErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public UserDetailRes getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalErrorException(UserErrorCode.NOT_FOUND));
        return UserMapper.toDetailRes(user);
    }
}
