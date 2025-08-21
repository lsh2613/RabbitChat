package com.rabbitmqprac.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Slf4j
@Profile("!test")
@Configuration
public class DataSourceConfiguration {

    private static final String MASTER_DATA_SOURCE = "MASTER";
    private static final String REPLICA_DATA_SOURCE = "REPLICA";

    @Bean
    @Qualifier(MASTER_DATA_SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        HikariDataSource dataSource = DataSourceBuilder
                .create()
                .type(HikariDataSource.class)
                .build();
        dataSource.setPoolName(MASTER_DATA_SOURCE);
        return dataSource;
    }

    @Bean
    @Qualifier(REPLICA_DATA_SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.replica")
    public DataSource replicaDataSource() {
        HikariDataSource dataSource = DataSourceBuilder
                .create()
                .type(HikariDataSource.class)
                .build();
        dataSource.setPoolName(REPLICA_DATA_SOURCE);
        return dataSource;
    }

    @Bean
    public DataSource routingDataSource(
            @Qualifier(MASTER_DATA_SOURCE) DataSource masterDataSource,
            @Qualifier(REPLICA_DATA_SOURCE) DataSource replicaDataSource
    ) {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(MASTER_DATA_SOURCE, masterDataSource);
        dataSourceMap.put(REPLICA_DATA_SOURCE, replicaDataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSource determinedDataSource = routingDataSource(masterDataSource(), replicaDataSource());
        return new LazyConnectionDataSourceProxy(determinedDataSource);
    }

    @Slf4j
    public static class RoutingDataSource extends AbstractRoutingDataSource {
        @Override
        protected Object determineCurrentLookupKey() {
            String lookupKey = TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? REPLICA_DATA_SOURCE : MASTER_DATA_SOURCE;
            log.info("Current DataSource type: {}", lookupKey);
            return lookupKey;
        }
    }
}
