package com.twin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableAsync
@SpringBootApplication
@EnableSwagger2
//@EnableDiscoveryClient
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class BootBasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootBasicApplication.class, args);
    }

}
