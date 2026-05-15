package com.rabbit.ai.mcp;


import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class McpConfig {

    @Value("${zhipu.apiKey}")
    private String apiKey;

    @Value("${zhipu.mcp.web-search-url}")
    private String webSearchUrl;

    @Bean
    public McpToolProvider mcpToolProvider() {
        McpTransport transport = new StreamableHttpMcpTransport.Builder()
                .url(webSearchUrl)
                .customHeaders(() -> Map.of("Authorization", "Bearer " + apiKey))
                .logRequests(true)
                .logResponses(true)
                .build();

        McpClient client = new DefaultMcpClient.Builder()
                .key("Rabbit")
                .transport(transport)
                .build();

        return McpToolProvider.builder()
                .mcpClients(client)
                .build();
    }

}
