package com.rabbitmqprac.domain.persistence.user.entity;

import com.rabbitmqprac.domain.persistence.common.model.DateAuditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "user")
public class User extends DateAuditable {

    private static String USERNAME_FORMAT = "USER_%d";
    private static Integer SEQUENCE_NAME = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static User create() {
        User user = new User();
        user.username = String.format(USERNAME_FORMAT, SEQUENCE_NAME++);
        return user;
    }
}
