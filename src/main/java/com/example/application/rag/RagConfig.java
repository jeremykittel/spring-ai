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
public class RagConfig {

    @Value("classpath:pdfDocs/*.pdf")
    private Resource[] pdfResources;

    @Bean
    VectorStore vectorStore(EmbeddingClient embeddingClient) {
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingClient);
        for (Resource pdfResource : pdfResources) {
            processPdfResource(simpleVectorStore, pdfResource);
        }
        return simpleVectorStore;
    }

    @SneakyThrows
    private void processPdfResource(SimpleVectorStore simpleVectorStore, Resource pdfResource) {
        log.info("Processing Resource {}", pdfResource.getFilename());
        var pdfReader = createPdfReader(pdfResource);
        var textSplitter = new TokenTextSplitter();
        simpleVectorStore.accept(textSplitter.apply(pdfReader.get()));
    }

    private PagePdfDocumentReader createPdfReader(Resource pdfResource) {
        var config = createPdfDocumentReaderConfig();
        return new PagePdfDocumentReader(pdfResource, config);
    }

    private PdfDocumentReaderConfig createPdfDocumentReaderConfig() {
        return PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(
                        new ExtractedTextFormatter.Builder()
                                .build())
                .build();
    }
}
