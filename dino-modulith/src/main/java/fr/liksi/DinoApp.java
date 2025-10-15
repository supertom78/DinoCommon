package fr.liksi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.modulith.Modulithic;

@Modulithic
@SpringBootApplication
@ConfigurationPropertiesScan
public class DinoApp {
    public static void main(String[] args) {
        SpringApplication.run(DinoApp.class, args);
    }
}
