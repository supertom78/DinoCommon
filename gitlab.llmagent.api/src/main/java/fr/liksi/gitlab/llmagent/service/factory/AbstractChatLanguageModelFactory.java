package fr.liksi.gitlab.llmagent.service.factory;

import dev.langchain4j.model.chat.ChatLanguageModel;
import fr.liksi.gitlab.llmagent.configuration.model.Langchain4jProperties;

public abstract class AbstractChatLanguageModelFactory{
    public abstract ChatLanguageModel buildChatModel(Langchain4jProperties langchain4jProperties);
}
