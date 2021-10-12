package me.bruceli.uaasource;


import me.bruceli.uaasource.configProperties.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
public class CliSecurityUaaSourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CliSecurityUaaSourceApplication.class, args);
    }

}
