package fr.liksi.gitlab.llmagent.configuration.model;

import fr.liksi.gitlab.llmagent.service.factory.AbstractChatLanguageModelFactory;
import fr.liksi.gitlab.llmagent.service.factory.GoogleAiGeminiChatModelFactory;
import fr.liksi.gitlab.llmagent.service.factory.OpenAiChatModelFactory;

public class Langchain4jProperties {
    private String apikey;
    private String modelName;
    private String temperature;
    private String timeout;
    private boolean logRequests;
    private boolean logResponses;

    public String getApikey() {
        return apikey;
    }

    public Langchain4jProperties setApikey(String apikey) {
        this.apikey = apikey;
        return this;
    }

    public String getModelName() {
        return modelName;
    }

    public Langchain4jProperties setModelName(String modelName) {
        this.modelName = modelName;
        return this;
    }

    public String getTemperature() {
        return temperature;
    }

    public Langchain4jProperties setTemperature(String temperature) {
        this.temperature = temperature;
        return this;
    }

    public String getTimeout() {
        return timeout;
    }

    public Langchain4jProperties setTimeout(String timeout) {
        this.timeout = timeout;
        return this;
    }

    public boolean isLogRequests() {
        return logRequests;
    }

    public Langchain4jProperties setLogRequests(boolean logRequests) {
        this.logRequests = logRequests;
        return this;
    }

    public boolean isLogResponses() {
        return logResponses;
    }

    public Langchain4jProperties setLogResponses(boolean logResponses) {
        this.logResponses = logResponses;
        return this;
    }

    public AbstractChatLanguageModelFactory getChatModelFactory() {
        if(modelName.contains("gemini")) {
            return new GoogleAiGeminiChatModelFactory();
        } else if(modelName.contains("gpt")) {
            return new OpenAiChatModelFactory();
        } else {
            throw new IllegalArgumentException("Unknown model name: " + modelName);
        }
    }
}
