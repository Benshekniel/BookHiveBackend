package App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class }) //enables no username or password needed
public class BookHiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookHiveApplication.class, args);
    }

}
