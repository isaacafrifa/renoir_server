package com.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class RenoirServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RenoirServerApplication.class, args);
    }


}
