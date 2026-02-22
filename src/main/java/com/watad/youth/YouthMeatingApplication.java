package com.watad.youth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.watad.entity")
@ComponentScan("com.watad")
@EnableJpaRepositories("com.watad.repo")
public class YouthMeatingApplication {

	public static void main(String[] args) {

		SpringApplication.run(YouthMeatingApplication.class, args);
	}

}
