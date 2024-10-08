package fr.liksi.gitlab.llmagent.configuration;

import fr.liksi.gitlab.llmagent.configuration.model.GitlabProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitlabConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "gitlab")
    public GitlabProperties gitlabProperties() {
        return new GitlabProperties();
    }
}
