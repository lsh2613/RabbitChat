package com.rabbitmqprac.member;

import com.rabbitmqprac.global.model.DateAuditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "MEMBER")
public class Member extends DateAuditable {

    private static String NICKNAME_FORMAT = "MEMBER_%d";
    private static Integer SEQUENCE_NAME = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member create() {
        Member member = new Member();
        member.nickname = String.format(NICKNAME_FORMAT, SEQUENCE_NAME++);
        return member;
    }
}
