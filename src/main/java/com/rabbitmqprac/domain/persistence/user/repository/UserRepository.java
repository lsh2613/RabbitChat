package com.rabbitmqprac.domain.persistence.user.repository;

import com.rabbitmqprac.domain.persistence.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    boolean existsByNickname(String nickname);
}
