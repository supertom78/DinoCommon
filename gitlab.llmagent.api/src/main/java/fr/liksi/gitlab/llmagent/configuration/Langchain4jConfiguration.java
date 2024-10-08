package fr.liksi.gitlab.llmagent.configuration;

import dev.langchain4j.model.chat.ChatLanguageModel;
import fr.liksi.gitlab.llmagent.configuration.model.Langchain4jProperties;
import fr.liksi.gitlab.llmagent.service.factory.GoogleAiGeminiChatModelFactory;
import fr.liksi.gitlab.llmagent.service.factory.OpenAiChatModelFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Langchain4jConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "langchain4j")
    public Langchain4jProperties langchain4jProperties() {
        return new Langchain4jProperties();
    }

    @Bean
    public ChatLanguageModel chatLanguageModel(Langchain4jProperties langchain4jProperties) {
        return langchain4jProperties.getChatModelFactory().buildChatModel(langchain4jProperties);
    }
}
