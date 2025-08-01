package com.rabbitmqprac.repository;

import com.rabbitmqprac.common.annotation.CustomJpaRepositoryTest;
import com.rabbitmqprac.common.container.MySQLTestContainer;
import com.rabbitmqprac.common.fixture.UserFixture;
import com.rabbitmqprac.common.helper.EntitySaver;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.domain.persistence.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@CustomJpaRepositoryTest
class UserRepositoryTest extends MySQLTestContainer {
    @Autowired
    private EntitySaver entitySaver;
    @Autowired
    private UserRepository userRepository;

    private static User user;

    @BeforeEach
    void setUp() {
        user = entitySaver.saveUser(UserFixture.FIRST_USER.toEntity());
    }

    @Test
    public void existsByUsername() {
        // given
        String username = user.getUsername();

        // when
        boolean exists = userRepository.existsByUsername(username);

        // then
        assertTrue(exists);
    }

    @Test
    public void findByUsername() {
        // given
        String username = user.getUsername();

        // when
        Optional<User> findUser = userRepository.findByUsername(username);

        // then
        assertTrue(findUser.isPresent());
        assertEquals(findUser.get().getId(), user.getId());
    }

    @Test
    public void existsByNickname() {
        // given
        String nickname = user.getNickname();

        // when
        boolean exists = userRepository.existsByNickname(nickname);

        // then
        assertTrue(exists);
    }
}