package fr.liksi.gitlab.llmagent.configuration;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import fr.liksi.gitlab.llmagent.service.GitlabLLMAgent;
import fr.liksi.gitlab.llmagent.service.GitlabTools;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitlabLLMAgentConfiguration {

    @Bean
    GitlabLLMAgent gitlabLLMAgent(ChatLanguageModel chatLanguageModel, GitlabTools gitlabTools) {
        return AiServices.builder(GitlabLLMAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(6))
                .tools(gitlabTools)
                .build();
    }

}
