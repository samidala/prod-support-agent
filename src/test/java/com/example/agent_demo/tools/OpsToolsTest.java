package com.example.agent_demo.tools;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class OpsToolsTest {

    @Test
    public void testSearchWiki() {
        // Mock dependencies
        EmbeddingModel embeddingModel = Mockito.mock(EmbeddingModel.class);
        EmbeddingStore<TextSegment> embeddingStore = Mockito.mock(EmbeddingStore.class);

        // Mock behavior
        when(embeddingModel.embed(anyString())).thenReturn(Response.from(new Embedding(new float[] { 0.1f })));

        EmbeddingMatch<TextSegment> match = new EmbeddingMatch<>(0.9, "id", new Embedding(new float[] { 0.1f }),
                TextSegment.from("Wiki Result"));
        when(embeddingStore.findRelevant(any(), anyInt(), anyDouble())).thenReturn(List.of(match));

        OpsTools tools = new OpsTools(embeddingModel, embeddingStore);
        String result = tools.searchWiki("checkout 500 error");
        assertTrue(result.contains("Wiki Result"));
    }

    @Test
    public void testConsultTeam() {
        // Mock dependencies (even if not used in this method, constructor needs them)
        EmbeddingModel embeddingModel = Mockito.mock(EmbeddingModel.class);
        EmbeddingStore<TextSegment> embeddingStore = Mockito.mock(EmbeddingStore.class);

        OpsTools tools = new OpsTools(embeddingModel, embeddingStore);
        String result = tools.consultTeam("Backend", "status?");
        assertTrue(result.contains("Backend Team"));
    }
}
