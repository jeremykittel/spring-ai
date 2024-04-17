package com.example.application.endpoints.ai;

import com.example.application.etl.PlainTextReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The DataLoadingService class is responsible for loading text documents and splitting them
 * into tokens using a token text splitter. It uses the PlainTextReader to load the text documents
 * and the VectorStore to store the resulting tokens.
 */
@Service
@Slf4j
public class DataLoadingService {
    private final VectorStore vectorStore;
    private final PlainTextReader plainTextReader;
    private final TokenTextSplitter tokenTextSplitter;

    public DataLoadingService(VectorStore vectorStore, PlainTextReader plainTextReader, TokenTextSplitter tokenTextSplitter) {
        this.vectorStore = vectorStore;
        this.plainTextReader = plainTextReader;
        this.tokenTextSplitter = tokenTextSplitter;
    }

    /**
     * Loads text documents and splits them into tokens using a token text splitter.
     */
    public void load() {
        try {
            log.info("Loading text documents...");
            List<Document> documents = plainTextReader.loadText();
            if (documents != null) {
                log.info("Splitting text into tokens...");
                vectorStore.accept(tokenTextSplitter.apply(documents));
                log.info("Text has been successfully loaded and split into tokens.");
            } else {
                log.warn("No documents were loaded. Skipping token splitting.");
            }
        } catch (Exception e) {
            log.error("An error occurred while loading text documents and splitting them into tokens", e);
            throw new RuntimeException("Failed to load and split text documents", e);
        }

    }
}
