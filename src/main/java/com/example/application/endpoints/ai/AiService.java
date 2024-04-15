package com.example.application.endpoints.ai;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
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
@BrowserCallable
@AnonymousAllowed
public class AiService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    @Value("classpath:prompts/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    /**
     * AiService class represents a service used to chat with an AI.
     */
    public AiService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    /**
     * Chat with the AI service using the given query.
     *
     * @param query The query string used for the chat.
     * @return The response from the AI service.
     */
    public String chat(String query) {
        String contentList = getSimilarDocumentsContent(query);
        Prompt prompt = createPrompt(query, contentList);
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }

    /**
     * Retrieves the content of similar documents based on the provided query.
     *
     * @param query The query string used to search for similar documents.
     * @return A string representation of the content of the similar documents, separated by line breaks.
     */
    private String getSimilarDocumentsContent(String query) {
        List<Document> similarDocuments = vectorStore.similaritySearch(query);
        return similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * Creates a prompt using the given query and content list.
     *
     * @param query The query string used as input for the prompt.
     * @param contentList A string representation of the content list, separated by line breaks.
     * @return The created prompt.
     */
    private Prompt createPrompt(String query, String contentList) {
        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Map<String, Object> promptParameters = new HashMap<>();
        promptParameters.put("input", query);
        promptParameters.put("documents", String.join("\n", contentList));
        return promptTemplate.create(promptParameters);
    }
}