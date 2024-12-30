package it.olegna.jpa.multitenancy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MultitenancyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultitenancyApplication.class, args);
	}
}
