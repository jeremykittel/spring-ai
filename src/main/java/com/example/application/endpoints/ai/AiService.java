package com.example.application.endpoints.ai;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Endpoint
@AnonymousAllowed
public class AiService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public AiService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    public String chat(String query) {

        List<Document> similarDocuments = vectorStore.similaritySearch(query);

        String information = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(
                """
                            You are a helpful assistant.
                            Use only the following information to answer the question.
                            Do not use any other information. If you do not know, simply answer: Unknown.

                            {information}
                        """);

        Message systemMessage = systemPromptTemplate.createMessage(Map.of("information", information));
        PromptTemplate promptTemplate = new PromptTemplate("{query}");
        Message message = promptTemplate.createMessage(Map.of("query", query));
        Prompt prompt = new Prompt(List.of(systemMessage, message));
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }
}