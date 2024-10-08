package fr.liksi.gitlab.llmagent.service.factory;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import fr.liksi.gitlab.llmagent.configuration.model.Langchain4jProperties;

import java.time.Duration;

public final class OpenAiChatModelFactory extends AbstractChatLanguageModelFactory {
    public ChatLanguageModel buildChatModel(Langchain4jProperties langchain4jProperties) {
        return OpenAiChatModel.builder()
                .apiKey(langchain4jProperties.getApikey())
                .modelName(langchain4jProperties.getModelName())
                .temperature(Double.parseDouble(langchain4jProperties.getTemperature()))
                .timeout(Duration.parse(langchain4jProperties.getTimeout()))
                .logResponses(langchain4jProperties.isLogResponses())
                .logRequests(langchain4jProperties.isLogRequests())
                .build();
    }
}
