package com.example.application.etl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The PlainTextReader class is responsible for loading plain text documents from a list of document resources.
 * It uses the TextReader class to read the text from each resource and creates a list of loaded documents.
 */
@Component
@Slf4j
public class PlainTextReader {

    @Value("classpath:docs/*.txt")
    private Resource[] documentResources;

    /**
     * Loads text documents from a list of document resources.
     *
     * @return a list of loaded documents
     */
    public List<Document> loadText() {
        List<Document> documents = new ArrayList<>();
        for (Resource resource : documentResources) {
            TextReader textReader = new TextReader(resource);
            textReader.getCustomMetadata().put("filename", "text-source.txt");
            documents.addAll(textReader.get());
        }
        return documents;
    }
}
