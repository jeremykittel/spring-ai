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

    public DataLoadingService(VectorStore vectorStore, PlainTextReader plainTextReader) {
        this.vectorStore = vectorStore;
        this.plainTextReader = plainTextReader;
    }


    /**
     * Loads text documents and splits them into tokens using a token text splitter.
     */
    public void load() {
        List<Document> documents = plainTextReader.loadText();
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
       vectorStore.accept(tokenTextSplitter.apply(documents));

    }
}
