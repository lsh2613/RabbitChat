package com.rabbitmqprac.common.annotation;

import com.rabbitmqprac.common.config.TestBeanConfig;
import com.rabbitmqprac.common.config.TestJpaConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestBeanConfig.class, TestJpaConfig.class})
@ActiveProfiles("test")
@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=create"})
public @interface CustomJpaRepositoryTest {
}
