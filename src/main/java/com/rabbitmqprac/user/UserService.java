package com.rabbitmqprac.user;


import com.rabbitmqprac.global.exception.GlobalErrorException;
import com.rabbitmqprac.user.dto.UserDetailRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User readById(Long memberId) {
        return userRepository.findById(memberId).
                orElseThrow(() -> new GlobalErrorException(UserErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> readUser(long memberId) {
        return userRepository.findById(memberId);
    }

    @Transactional(readOnly = true)
    public UserDetailRes getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalErrorException(UserErrorCode.NOT_FOUND));
        return UserMapper.toDetailRes(user);
    }
}
