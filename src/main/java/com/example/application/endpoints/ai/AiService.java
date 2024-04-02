package com.example.application.endpoints.ai;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Endpoint
@AnonymousAllowed
public class AiService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:prompts/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    public AiService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }


    /**
     * Service method used for communicating with ChatGPT and embedding context
     *
     * @param query the prompt to send to openai api
     * @return the response with augmented data
     */
    public String chat(String query) {
        List<Document> similarDocuments = vectorStore.similaritySearch(query);
        String contentList = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));
        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Map<String, Object> promptParameters = new HashMap<>();
        promptParameters.put("input", query);
        promptParameters.put("documents", String.join("\n", contentList));
        Prompt prompt = promptTemplate.create(promptParameters);
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }
}