package me.bruceli.uaa;


import me.bruceli.uaa.configProperties.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class CliSecurityUaa8054Application {

    public static void main(String[] args) {
        SpringApplication.run(CliSecurityUaa8054Application.class, args);
    }

}
