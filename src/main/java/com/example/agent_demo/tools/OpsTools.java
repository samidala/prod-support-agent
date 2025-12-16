package com.example.agent_demo.tools;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpsTools {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    public OpsTools(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    @Tool("Search the internal Wiki and Documentation for a query")
    public String searchWiki(String query) {
        System.out.println("Searching Wiki (Vector Store) for: " + query);

        List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.findRelevant(
                embeddingModel.embed(query).content(),
                3, // max results
                0.6 // min score
        );

        if (relevant.isEmpty()) {
            return "No relevant wiki articles found for: " + query;
        }

        return relevant.stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.joining("\n---\n"));
    }

    @Tool("Read a Runbook for a specific service")
    public String readRunbook(String serviceName) {
        // Keep mocked for now, or could also be RAG
        System.out.println("Reading Runbook for: " + serviceName);
        if (serviceName.toLowerCase().contains("checkout")) {
            return "Runbook: Checkout Service Recovery\n" +
                    "1. Check CPU and Memory usage.\n" +
                    "2. Verify DB connection pool status.\n" +
                    "3. If DB connections are full, restart the service pods: `kubectl rollout restart deployment checkout-service`.\n"
                    +
                    "4. Rollback to previous version if restart fails.";
        }
        return "No runbook found for service: " + serviceName;
    }

    @Tool("Consult a specific Team Expert (Backend, Architect, Infra, DB) with a question")
    public String consultTeam(String teamName, String question) {
        System.out.println("Consulting " + teamName + " team: " + question);
        switch (teamName.toLowerCase()) {
            case "backend":
                return "Backend Team: We deployed version v2.1 about an hour ago. It included some changes to the payment processing logic.";
            case "db":
            case "database":
                return "DB Team: We are seeing a spike in active connections. The connection pool seems to be maxed out for the checkout service.";
            case "infra":
            case "infrastructure":
                return "Infra Team: All underlying nodes are healthy. Network latency is normal. No outages reported.";
            case "architect":
                return "Architect: If the DB pool is exhausted, it might be a leak. Did we close connections in the new payment logic?";
            default:
                return teamName + " Team: We are not aware of any issues.";
        }
    }
}
