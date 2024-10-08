package fr.liksi.gitlab.llmagent.service.factory;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import fr.liksi.gitlab.llmagent.configuration.model.Langchain4jProperties;

import java.time.Duration;

public final class GoogleAiGeminiChatModelFactory extends AbstractChatLanguageModelFactory {
    public ChatLanguageModel buildChatModel(Langchain4jProperties langchain4jProperties) {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(langchain4jProperties.getApikey())
                .modelName(langchain4jProperties.getModelName())
                .temperature(Double.parseDouble(langchain4jProperties.getTemperature()))
                .timeout(Duration.ofSeconds(60))
                .candidateCount(1)
                .allowCodeExecution(true)
                .logRequestsAndResponses(true)
                .responseFormat(ResponseFormat.TEXT) // or .responseFormat(ResponseFormat.builder()...build())
                .build();
    }
}
