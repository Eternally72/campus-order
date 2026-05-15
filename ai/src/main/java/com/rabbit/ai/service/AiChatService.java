package com.rabbit.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

/**
 * AI对话服务接口
 */
public interface AiChatService {

    /**
     * AI对话（同步）
     */
    @SystemMessage(fromResource = "prompt/prompt-chat.txt")
    String chat(@MemoryId int memoryId,@UserMessage String message);

    /**
     * AI对话（流式）
     */
    @SystemMessage(fromResource = "prompt/prompt-chat.txt")
    Flux<String> chatStream(@MemoryId int memoryId,@UserMessage String message);

    /**
     * 多模态
     */
    @SystemMessage(fromResource = "prompt/prompt-multi-model.txt")
    Flux<String> multiModelChat(@MemoryId int memoryId, @UserMessage dev.langchain4j.data.message.UserMessage message);
}

