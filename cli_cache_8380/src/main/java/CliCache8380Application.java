import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("me.bruceli.cache")
@EnableCaching
public class CliCache8380Application {
    public static void main(String[] args) {

        SpringApplication.run(CliCache8380Application.class,args);
    }
}
