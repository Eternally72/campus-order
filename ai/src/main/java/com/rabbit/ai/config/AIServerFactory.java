package com.rabbit.ai.config;

import com.rabbit.ai.service.AiChatService;
import com.rabbit.ai.tools.DatabaseTools;
import dev.langchain4j.community.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.community.model.zhipu.ZhipuAiStreamingChatModel;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIServerFactory {

    @Resource
    private ZhipuAiChatModel zhipuAiChatModel;

    @Resource
    private ZhipuAiStreamingChatModel  zhipuAiStreamingChatModel;

    @Resource
    private ContentRetriever contentRetriever;

    @Resource
    private McpToolProvider mcpToolProvider;

    @Bean
    public AiChatService aiChatService() {

        return AiServices.builder(AiChatService.class)
                .chatModel(zhipuAiChatModel)
                .streamingChatModel(zhipuAiStreamingChatModel)
                .tools(new DatabaseTools())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(contentRetriever)
                .chatMemoryProvider(memory->MessageWindowChatMemory.withMaxMessages(10))
                .toolProvider(mcpToolProvider)
                .build();
    }
}
