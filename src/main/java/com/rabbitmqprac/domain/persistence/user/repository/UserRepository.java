package com.rabbitmqprac.domain.persistence.user.repository;

import com.rabbitmqprac.domain.persistence.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
