package com.rabbitmqprac.common.helper;

import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.domain.persistence.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class EntitySaver {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
