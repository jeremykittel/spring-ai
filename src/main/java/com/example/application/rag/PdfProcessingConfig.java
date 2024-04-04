package com.example.application.rag;

import lombok.SneakyThrows;
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

@Slf4j
@Configuration
public class PdfProcessingConfig {

    @Value("classpath:pdfDocs/*.pdf")
    private Resource[] documentResources;

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

    @SneakyThrows
    private void addResourceToStore(SimpleVectorStore simpleVectorStore, TokenTextSplitter textSplitter,
                                    PdfDocumentReaderConfig config, Resource documentResource) {
        log.info("Processing Resource {}", documentResource.getFilename());

        PagePdfDocumentReader documentReader = new PagePdfDocumentReader(documentResource, config);

        simpleVectorStore.accept(textSplitter.apply(documentReader.get()));
    }

    private PdfDocumentReaderConfig createPdfDocumentReaderConfig() {
        return PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().build())
                .build();
    }
}
