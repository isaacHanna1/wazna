package com.watad.youth;

import com.watad.entity.Role;
import com.watad.services.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.watad.entity")
@ComponentScan("com.watad")
public class YouthMeatingApplication {

	public static void main(String[] args) {
		SpringApplication.run(YouthMeatingApplication.class, args);
	}

	@Bean
	CommandLineRunner initRoles(RoleService roleService) {
		return args -> {
			roleService.findOrCreate("ROLE_YOUTH");
			roleService.findOrCreate("ROLE_SERVER");
			roleService.findOrCreate("ROLE_SUPER");

		};
	}
}
