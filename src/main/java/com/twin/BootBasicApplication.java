package com.twin;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.concurrent.TimeUnit;

/**
 * @author itranlin
 */
@EnableAsync
@SpringBootApplication(exclude = RedisAutoConfiguration.class)
@EnableSwagger2
@EnableDiscoveryClient
public class BootBasicApplication {

    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(BootBasicApplication.class, args);
        while(true){
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            String username = environment.getProperty("user.name");
            String age = environment.getProperty("user.age");
            System.out.println("username:"+username+" | age:"+age);
            TimeUnit.SECONDS.sleep(1);
        }
    }

}
