package com.pickple.common_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CommonModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonModuleApplication.class, args);
    }

}
