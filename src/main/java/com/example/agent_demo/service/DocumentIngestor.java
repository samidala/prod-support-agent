package com.example.agent_demo.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Component
public class DocumentIngestor implements CommandLineRunner {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    public DocumentIngestor(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- Starting Document Ingestion ---");
        try {
            File wikiDir = ResourceUtils.getFile("classpath:wiki");
            Path path = wikiDir.toPath();

            List<Document> documents = FileSystemDocumentLoader.loadDocuments(path,
                    new dev.langchain4j.data.document.parser.TextDocumentParser());

            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                    .documentSplitter(DocumentSplitters.recursive(500, 0))
                    .embeddingModel(embeddingModel)
                    .embeddingStore(embeddingStore)
                    .build();

            ingestor.ingest(documents);
            System.out.println("--- Ingested " + documents.size() + " documents into Vector Store ---");
        } catch (Exception e) {
            System.err.println("Failed to ingest documents: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
