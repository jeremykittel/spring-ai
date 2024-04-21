package com.example.application.endpoints.ai;

import com.example.application.etl.PlainTextReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class DataLoadingServiceUnitTest {
    @Mock
    private VectorStore vectorStore;
    @Mock
    private PlainTextReader plainTextReader;
    @Mock
    private TokenTextSplitter tokenTextSplitter;
    @Mock
    private Document document;

    private final List<Document> documents = new ArrayList<>();

    @BeforeEach
    public void setup() {
        documents.add(document);
    }

    @Test
    public void testLoad() {
        when(plainTextReader.loadText()).thenReturn(documents);
        DataLoadingService dataService = instantiateDataService();

        dataService.load();

        verify(vectorStore, times(1)).accept(tokenTextSplitter.apply(documents));
    }

    @Test
    public void testLoad_Exception() {
        doThrow(RuntimeException.class).when(plainTextReader).loadText();

        DataLoadingService dataService = instantiateDataService();

        assertThrows(RuntimeException.class, dataService::load);
    }

    @Test
    public void testLoad_NullDocuments() {
        when(plainTextReader.loadText()).thenReturn(null); // return null documents list

        DataLoadingService dataService = instantiateDataService();
        dataService.load();

        verify(vectorStore, times(0)).accept(any()); // verify vectorStore.accept is not called
    }

    private DataLoadingService instantiateDataService() {
        return new DataLoadingService(vectorStore, plainTextReader, tokenTextSplitter);
    }
}