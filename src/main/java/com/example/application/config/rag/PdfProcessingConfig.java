package com.example.application.config.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * Configuration class for PDF processing.
 */
@Configuration
@Slf4j
public class PdfProcessingConfig {

    @Value("classpath:pdfDocs/*.pdf")
    private Resource[] documentResources;

    /**
     * Retrieves a vector store containing document resources.
     *
     * @param embeddingClient The embedding client.
     * @return A vector store containing document resources.
     */
    @Bean
    VectorStore vectorStore(EmbeddingClient embeddingClient) {

        var config = createPdfDocumentReaderConfig();
        var textSplitter = new TokenTextSplitter();
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingClient);

        for (Resource documentResource : documentResources) {
            addResourceToStore(simpleVectorStore, textSplitter, config, documentResource);
        }

        return simpleVectorStore;
    }

    /**
     * Adds a resource to the vector store.
     *
     * @param simpleVectorStore The vector store to add the resource to.
     * @param textSplitter      The token text splitter.
     * @param config            The configuration for PDF document reader.
     * @param documentResource  The resource to add to the vector store.
     */
    private void addResourceToStore(SimpleVectorStore simpleVectorStore, TokenTextSplitter textSplitter,
                                    PdfDocumentReaderConfig config, Resource documentResource) {
        log.info("Processing Resource {}", documentResource.getFilename());

        PagePdfDocumentReader documentReader = new PagePdfDocumentReader(documentResource, config);

        simpleVectorStore.accept(textSplitter.apply(documentReader.get()));
    }

    /**
     * Creates a {@link PdfDocumentReaderConfig} for PDF document processing.
     *
     * @return A {@link PdfDocumentReaderConfig} instance.
     */
    private PdfDocumentReaderConfig createPdfDocumentReaderConfig() {
        return PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().build())
                .build();
    }
}
