package me.bruceli.log4j2;

import me.bruceli.log4j2.listener.Log4j2GetApplicationPropertiesListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CliLog4j28003Application {
    public static void main(String[] args) {

        SpringApplication.run(CliLog4j28003Application.class,args);
        /*SpringApplication app = new SpringApplication(CliLog4j28003Application.class);
        app.addListeners(new Log4j2GetApplicationPropertiesListener());
        app.run(args);*/
    }
}
