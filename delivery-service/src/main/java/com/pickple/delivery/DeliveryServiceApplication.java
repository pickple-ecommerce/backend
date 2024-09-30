package com.pickple.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableCassandraRepositories
public class DeliveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryServiceApplication.class, args);
    }

}
