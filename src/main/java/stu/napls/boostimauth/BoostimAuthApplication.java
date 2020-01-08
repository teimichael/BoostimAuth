package stu.napls.boostimauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BoostimAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoostimAuthApplication.class, args);
    }

}
