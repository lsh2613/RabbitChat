package com.rabbitmqprac.domain.persistence.oauth.entity;

import com.rabbitmqprac.domain.persistence.common.model.DateAuditable;
import com.rabbitmqprac.domain.persistence.oauth.constant.OauthProvider;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "oauth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Oauth extends DateAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OauthProvider oauthProvider;
    @Column(nullable = false)
    private String sub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Oauth of(OauthProvider provider, String oauthId, User user) {
        Oauth oauth = new Oauth();
        oauth.oauthProvider = provider;
        oauth.sub = oauthId;
        oauth.user = user;
        return oauth;
    }
}
