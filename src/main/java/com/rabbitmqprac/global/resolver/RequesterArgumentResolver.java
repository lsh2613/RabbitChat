package com.rabbitmqprac.global.resolver;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.rabbitmqprac.global.annotation.Requester;
import com.rabbitmqprac.global.constant.TokenType;
import com.rabbitmqprac.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class RequesterArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenUtil tokenUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Requester.class) &&
                parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = tokenUtil.extractToken((HttpServletRequest) webRequest.getNativeRequest(), TokenType.ACCESS_TOKEN);
        DecodedJWT decodedJWT = tokenUtil.decodedJWT(token);
        Long id = decodedJWT.getClaim("id").asLong();

        return id;
    }

}