package me.bruceli.gateway;

import me.bruceli.gateway.configProperties.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.context.ConfigurableReactiveWebApplicationContext;
import org.springframework.cloud.bootstrap.encrypt.RsaProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;


@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class CliGateway9527Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(CliGateway9527Application.class, args);


    }
}

