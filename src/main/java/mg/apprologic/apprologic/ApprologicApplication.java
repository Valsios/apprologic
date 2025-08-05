package mg.apprologic.apprologic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApprologicApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApprologicApplication.class, args);
    }

}
