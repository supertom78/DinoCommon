package fr.liksi.dinorepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication()
public class DinorepoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DinorepoApplication.class, args);
	}

}
