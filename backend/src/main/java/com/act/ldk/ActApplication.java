package com.act.ldk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ActApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActApplication.class, args);
    }

}
