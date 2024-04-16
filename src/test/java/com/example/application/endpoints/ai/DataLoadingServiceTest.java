package com.example.application.endpoints.ai;

import com.example.application.etl.PlainTextReader;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import java.util.List;
import static org.mockito.Mockito.*;

/**
 * The DataLoadingServiceTest class will test the DataLoadingService class,
 * focusing on the load method which involves the loading and splitting of text documents.
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
public class DataLoadingServiceTest {

    /**
     * This test validates the correct operation of the load() method.
     * It verifies whether the vector store accepts the split tokens obtained from the PlainTextReader.
     */
    @Test
    public void testLoad() {
        // Mock dependencies
        VectorStore mockVectorStore = mock(VectorStore.class);
        PlainTextReader mockPlainTextReader = mock(PlainTextReader.class);
        List<Document> mockDocuments = List.of(mock(Document.class));

        // Mock interaction
        when(mockPlainTextReader.loadText()).thenReturn(mockDocuments);
      
        // Test instantiation
        DataLoadingService dataService = new DataLoadingService(mockVectorStore, mockPlainTextReader);
      
        // Call the function under test
        dataService.load();
      
        // Verification
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        verify(mockVectorStore, times(1)).accept(tokenTextSplitter.apply(mockDocuments));
    }
}