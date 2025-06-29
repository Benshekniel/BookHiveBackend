package App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class }) //enables no username or password needed
@ComponentScan(basePackages = { "controller", "service", "model" ,"App"})
@EnableJpaRepositories(basePackages = "model.repo")
@EntityScan(basePackages = "model.entity")
public class BookHiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookHiveApplication.class, args);
    }

}
