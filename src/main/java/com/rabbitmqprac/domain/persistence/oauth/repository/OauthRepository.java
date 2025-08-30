package com.rabbitmqprac.domain.persistence.oauth.repository;

import com.rabbitmqprac.domain.persistence.oauth.constant.OauthProvider;
import com.rabbitmqprac.domain.persistence.oauth.entity.Oauth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthRepository extends JpaRepository<Oauth, Long> {
    Optional<Oauth> findBySubAndOauthProvider(String sub, OauthProvider oauthProvider);

    boolean existsBySubAndOauthProvider(String sub, OauthProvider oauthProvider);
}
